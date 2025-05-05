package com.banula.ocpi.model.enums;

/**
 * The category of an image to obtain the correct usage in a user presentation. The category has
 * to be set accordingly to the image content in order to guarantee the right usage.
 */
public enum ImageCategory {
    /**
     * Photo of the physical device that contains one or more EVSEs.
     */
    CHARGER,
    /**
     * Location entrance photo. Should show the car entrance to the location from street side.
     */
    ENTRANCE,
    /**
     * Location overview photo.
     */
    LOCATION,
    /**
     * Logo of an associated roaming network to be displayed with the EVSE for example in lists, maps
     * and detailed information views.
     */
    NETWORK,
    /**
     * Logo of an associated roaming network to be displayed with the EVSE for example in lists, maps
     * and detailed information views.
     */
    OPERATOR,
    /**
     * Other
     */
    OTHER,
    /**
     * Logo of the charge point owner, for example a local store, to be displayed in the EVSEs detailed
     * information view
     */
    OWNER
}
