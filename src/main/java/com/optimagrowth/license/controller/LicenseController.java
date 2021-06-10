package com.optimagrowth.license.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optimagrowth.license.model.License;
import com.optimagrowth.license.service.LicenseService;

import java.util.Locale;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value="v1/organization/{organizationId}/license")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @RequestMapping(value="/{licenseId}",method = RequestMethod.GET)
    public ResponseEntity<License> getLicense( @PathVariable("organizationId") String organizationId,
                                               @PathVariable("licenseId") String licenseId) {

        License license = licenseService.getLicense(licenseId, organizationId);
        addHateLinks(license, organizationId);
        return ResponseEntity.ok(license);
    }

    @PutMapping
    public ResponseEntity<String> updateLicense(@PathVariable("organizationId") String organizationId,
                                                @RequestBody License request) {
        return ResponseEntity.ok(licenseService.updateLicense(request, organizationId));
    }

    @PostMapping
    public ResponseEntity<String> createLicense(
            @PathVariable("organizationId") String organizationId,
            @RequestBody License request,
            @RequestHeader(value = "Accept-Language",required = false) Locale locale){
        return ResponseEntity.ok(licenseService.
                createLicense(request, organizationId, locale));
    }

    @DeleteMapping(value="/{licenseId}")
    public ResponseEntity<String> deleteLicense(@PathVariable("organizationId") String organizationId,
                                                @PathVariable("licenseId") String licenseId) {
        return ResponseEntity.ok(licenseService.deleteLicense(licenseId, organizationId));
    }

    private void addHateLinks(License license, String organizationId) {
        license.add(
                linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId())).withSelfRel(),
                linkTo(methodOn(LicenseController.class).createLicense(organizationId, license, null)).withRel("createLicense"),
                linkTo(methodOn(LicenseController.class).updateLicense(organizationId, license)).withRel("updateLicense"),
                linkTo(methodOn(LicenseController.class).deleteLicense(organizationId, license.getLicenseId())).withRel("deleteLicense"));
    }
}