package com.kpn.ecommerce.singleshop.productservice.simonly;

import com.kpn.ecommerce.singleshop.productservice.business.CompletenessProcessor;
import com.kpn.ecommerce.singleshop.productservice.dto.BundleUnitType;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits;
import com.kpn.ecommerce.singleshop.productservice.dto.CompletenessBenefits.Completeness;
import com.kpn.ecommerce.singleshop.productservice.dto.ContractDuration;
import com.kpn.ecommerce.singleshop.productservice.dto.DataBundleDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SimOnlyDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SubscriptionDto;
import com.kpn.ecommerce.singleshop.productservice.dto.SubscriptionDto.SubscriptionDtoBuilder;
import com.kpn.ecommerce.singleshop.productservice.dto.VoiceSmsBundleDto;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.AmountUnitName;
import com.kpn.ecommerce.singleshop.productservice.simonly.domain.SubscriptionBundle;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@PropertySource("classpath:simonly.properties")
@Component
class SimOnlyMapper {

  private static final String REGEX_NUMERIC = "-?\\d+(\\.\\d+)?";

  private final Long defaultProductId;
  private final Long defaultSubscriptionId;
  private final Double setupFee;

  @Autowired
  SimOnlyMapper(
      @Value("${simonly.defaultProductId:1090537502}") final Long defaultProductId,
      @Value("${simonly.defaultSubscriptionId:751358035}") final Long defaultSubscriptionId,
      @Value("${simonly.setupFee:25.0}") final Double setupFee) {
    this.defaultProductId = defaultProductId;
    this.defaultSubscriptionId = defaultSubscriptionId;
    this.setupFee = setupFee;
  }

  SimOnlyDto toResponse(final CompletenessBenefits completenessBenefits,
      @NonNull Iterable<SubscriptionBundle> subscriptionsBundles) {

    final List<SubscriptionDto> subscriptionDtos = StreamSupport
        .stream(subscriptionsBundles.spliterator(), false)
        .map(SimOnlyMapper::toResponse)
        .collect(Collectors.toList());

    return new SimOnlyDto(setupFee, defaultProduct(),
        subscriptionDtos.stream().map(applyBenefits(completenessBenefits))
            .collect(Collectors.toList()),
        completenessBenefits);
  }

  private String defaultProduct() {
    return String.format("p%d:s%d", defaultProductId, defaultSubscriptionId);
  }

  private static String createId(final Completeness completeness,
      SubscriptionDto subscriptionDto) {
    return String.format("%s|%s", subscriptionDto.getId(), completeness.description());
  }

  private static Function<SubscriptionDto, SubscriptionDto> applyBenefits(
      final CompletenessBenefits completenessBenefits) {
    return subscription -> {
      final SubscriptionDtoBuilder subscriptionDtoBuilder = subscription.toBuilder();

      //doubling the data
      Optional.ofNullable(subscription.getData()).ifPresent(data -> {
        if (isNumeric(subscription.getData().getAmount())) {
          final String amount = String
              .valueOf(Integer.parseInt(subscription.getData().getAmount()) *
                  CompletenessProcessor.bundleMultiplier(completenessBenefits));
          subscriptionDtoBuilder
              .data(new DataBundleDto(amount, BundleUnitType.GB));
        } else {
          log.warn("Unable to double data on the bundle, [{}] is not a numeric value.",
              subscription.getData().getAmount());
        }
      });

      //doubling the roaming
      Optional.ofNullable(subscription.getDataRoaming()).ifPresent(roam -> {
        if (isNumeric(subscription.getDataRoaming().getAmount())) {
          final String amount = String
              .valueOf(Integer.parseInt(subscription.getDataRoaming().getAmount()) *
                  CompletenessProcessor.bundleMultiplier(completenessBenefits));
          subscriptionDtoBuilder.dataRoaming(new DataBundleDto(amount,
              BundleUnitType.GB));
        } else {
          log.warn("Unable to double data on the bundle, [{}] is not a numeric value.",
              subscription.getDataRoaming().getAmount());
        }
      });

      //doubling the roaming
      Optional.ofNullable(subscription.getVoiceSmsNational()).ifPresent(voiceSms -> {
        if (isNumeric(subscription.getVoiceSmsNational().getAmount())) {
          final String amount = String
              .valueOf(Integer.parseInt(subscription.getVoiceSmsNational().getAmount()) *
                  CompletenessProcessor.bundleMultiplier(completenessBenefits));
          subscriptionDtoBuilder
              .voiceSmsNational(
                  new VoiceSmsBundleDto(amount, BundleUnitType.VOICE_SMS));
        } else {
          log.warn("Unable to double data on the bundle, [{}] is not a numeric value.",
              subscription.getVoiceSmsNational().getAmount());
        }
      });

      return subscriptionDtoBuilder
          .id(subscription.getId())
          .actualPrice(
              subscription.getActualPrice() - CompletenessProcessor.discount(completenessBenefits))
          .build();
    };
  }


  private static SubscriptionDto toResponse(SubscriptionBundle bundle) {
    SubscriptionDto response = SubscriptionDto
        .builder()
        .id(bundle.getId())
        .name(bundle.getName())
        .duration(ContractDuration.fromValue(bundle.getDuration()))
        .actualPrice(bundle.getActualPrice())
        .listPrice(bundle.getListPrice())
        .slug(asSlug(bundle.getName()))
        .build();
    // mapping the bundles to the objects - changing state because this method
    // should change state anyways
    return mapBundles(response, bundle);
  }

  private static String asSlug(final String productName) {
    Assert.notNull(productName, "productName is null.");
    return productName.toLowerCase().replace("kpn ", "").replace(" ", "-");
  }

  private static SubscriptionDto mapBundles(final SubscriptionDto dto,
      final SubscriptionBundle subscriptionBundle) {

    final Set<AmountUnitName> setOfBundles = subscriptionBundle.getBundles().stream()
        .map(AmountUnitName::of)
        .collect(Collectors.toSet());
    setOfBundles.stream().sorted(Comparator.comparing(AmountUnitName::getAmount).reversed())
        .forEach(amountUnitName -> {
          switch (amountUnitName.getBundle()) {
            case DATA:
              if (Optional.ofNullable(dto.getData()).isPresent()) {
                log.trace("data has been set already.");
              } else {
                final DataBundleDto dataBundle = new DataBundleDto(amountUnitName.getAmount(),
                    BundleUnitType.GB);
                dto.setData(dataBundle);
              }
              break;
            case ROAM:
              if (Optional.ofNullable(dto.getDataRoaming()).isPresent()) {
                log.trace("data has been set already.");
              } else {
                final DataBundleDto roamingDataBundle = new DataBundleDto(
                    amountUnitName.getAmount(),
                    BundleUnitType.GB);
                dto.setDataRoaming(roamingDataBundle);
              }
              break;

            case VOICE_SMS:
              /* fallthrough */
            case COMB:
              if (Optional.ofNullable(dto.getVoiceSmsNational()).isPresent()) {
                log.trace("data has been set already.");
              } else {
                final VoiceSmsBundleDto comb = new VoiceSmsBundleDto(amountUnitName.getAmount(),
                    BundleUnitType.VOICE_SMS);
                dto.setVoiceSmsNational(comb);
              }
              break;

            default:
              throw new UnsupportedOperationException(
                  String
                      .format("bundle with name '%s' is not recognized.",
                          amountUnitName.getBundle()));
          }
        });
    return dto;
  }

  private static boolean isNumeric(final String strNum) {
    return strNum.matches(REGEX_NUMERIC);
  }

}
