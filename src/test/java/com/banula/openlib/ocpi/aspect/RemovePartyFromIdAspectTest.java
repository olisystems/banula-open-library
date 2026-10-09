package com.banula.openlib.ocpi.aspect;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.banula.openlib.ocpi.annotation.RemovePartyFromId;
import com.banula.openlib.ocpi.model.dto.EvseDTO;
import com.banula.openlib.ocpi.model.dto.LocationDTO;
import com.banula.openlib.ocpi.model.vo.EVSE;

class RemovePartyFromIdAspectTest {

    public static class LocationEndpoint {
        LocationDTO savedLocation;
        EVSE savedEvse;

        @RemovePartyFromId
        public void putLocation(@PathVariable("countryCode") String countryCode,
                @PathVariable("partyId") String partyId, @RequestBody LocationDTO location) {
            savedLocation = location;
        }

        @RemovePartyFromId
        public void putEvse(@PathVariable("countryCode") String countryCode,
                @PathVariable("partyId") String partyId, @RequestBody EVSE evse) {
            savedEvse = evse;
        }
    }

    private LocationEndpoint proxied(LocationEndpoint target) {
        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.setProxyTargetClass(true);
        factory.addAspect(new RemovePartyFromIdAspect());
        return factory.getProxy();
    }

    @Test
    void locationIdsAreStrippedButEvseIdKeepsItsPrefix() {
        LocationEndpoint target = new LocationEndpoint();
        EvseDTO evse = new EvseDTO();
        evse.setUid("DE*ABC*CP1*1");
        evse.setEvseId("DE*ABC*E123*1");
        LocationDTO location = new LocationDTO();
        location.setId("DE*ABC*LOC1");
        location.setEvses(List.of(evse));

        proxied(target).putLocation("DE", "ABC", location);

        assertEquals("LOC1", target.savedLocation.getId());
        assertEquals("CP1*1", target.savedLocation.getEvses().get(0).getUid());
        assertEquals("DE*ABC*E123*1", target.savedLocation.getEvses().get(0).getEvseId());
    }

    @Test
    void evseBodyKeepsItsEvseIdPrefix() {
        LocationEndpoint target = new LocationEndpoint();
        EVSE evse = new EVSE();
        evse.setUid("DE*ABC*CP1*1");
        evse.setEvseId("DE*ABC*E123*1");

        proxied(target).putEvse("DE", "ABC", evse);

        assertEquals("CP1*1", target.savedEvse.getUid());
        assertEquals("DE*ABC*E123*1", target.savedEvse.getEvseId());
    }
}
