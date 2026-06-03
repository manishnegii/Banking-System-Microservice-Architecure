package com.spring.user_service.utility;

import com.spring.user_service.entity.DocumentType;

public class MaskingUtils {

    private static final String MASK = "X";

    private MaskingUtils() {
    }

    public static String maskDocument(
            String documentNumber,
            DocumentType documentType
    ) {

        if (documentNumber == null ||
                documentNumber.isBlank()) {

            return documentNumber;
        }

        return switch (documentType) {

            case PAN ->
                    maskPan(documentNumber);

            case AADHAAR ->
                    maskAadhaar(documentNumber);

            case PASSPORT ->
                    maskPassport(documentNumber);

            case DRIVING_LICENSE ->
                    maskDrivingLicense(documentNumber);

            case VOTER_ID ->
                    maskVoterId(documentNumber);

            default ->
                    genericMask(documentNumber);
        };
    }

    private static String maskPan(String pan) {

        if (pan.length() <= 4) {
            return pan;
        }

        return MASK.repeat(6) +
                pan.substring(pan.length() - 4);
    }

    private static String maskAadhaar(
            String aadhaar
    ) {

        if (aadhaar.length() <= 4) {
            return aadhaar;
        }

        return MASK.repeat(8) +
                aadhaar.substring(aadhaar.length() - 4);
    }

    private static String maskPassport(
            String passport
    ) {

        if (passport.length() <= 4) {
            return passport;
        }

        return MASK.repeat(
                passport.length() - 4
        ) + passport.substring(
                passport.length() - 4
        );
    }

    private static String maskDrivingLicense(
            String license
    ) {

        return genericMask(license);
    }

    private static String maskVoterId(
            String voterId
    ) {

        return genericMask(voterId);
    }

    private static String genericMask(
            String value
    ) {

        if (value.length() <= 4) {
            return value;
        }

        return MASK.repeat(
                value.length() - 4
        ) + value.substring(
                value.length() - 4
        );
    }
}


