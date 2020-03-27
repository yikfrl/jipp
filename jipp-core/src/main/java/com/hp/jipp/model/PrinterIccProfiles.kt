// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
//
// DO NOT MODIFY. Code is auto-generated by genTypes.py. Content taken from registry at
// https://www.iana.org/assignments/ipp-registrations/ipp-registrations.xml, updated on 2020-02-20
@file:Suppress("MaxLineLength", "WildcardImport")

package com.hp.jipp.model

import com.hp.jipp.encoding.* // ktlint-disable no-wildcard-imports

/**
 * Data object corresponding to a "printer-icc-profiles" collection as defined in:
 * [PWG5100.13](https://ftp.pwg.org/pub/pwg/candidates/cs-ippjobprinterext3v10-20120727-5100.13.pdf).
 */
@Suppress("RedundantCompanionReference", "unused")
data class PrinterIccProfiles
constructor(
    var profileName: String? = null,
    var profileUrl: java.net.URI? = null
) : AttributeCollection {

    /** Construct an empty [PrinterIccProfiles]. */
    constructor() : this(null, null)

    /** Produce an attribute list from members. */
    override val attributes: List<Attribute<*>> by lazy {
        listOfNotNull(
            profileName?.let { Types.profileName.of(it) },
            profileUrl?.let { Types.profileUrl.of(it) }
        )
    }

    /** Type for attributes of this collection */
    class Type(override val name: String) : AttributeCollection.Type<PrinterIccProfiles>(PrinterIccProfiles)

    /** All member names as strings. */
    object Name {
        /** "profile-name" member name */
        const val profileName = "profile-name"
        /** "profile-url" member name */
        const val profileUrl = "profile-url"
    }

    /** Types for each member attribute. */
    object Types {
        val profileName = NameType(Name.profileName)
        val profileUrl = UriType(Name.profileUrl)
    }

    /** Defines types for each member of [PrinterIccProfiles] */
    companion object : AttributeCollection.Converter<PrinterIccProfiles> {
        override fun convert(attributes: List<Attribute<*>>): PrinterIccProfiles =
            PrinterIccProfiles(
                extractOne(attributes, Types.profileName)?.value,
                extractOne(attributes, Types.profileUrl)
            )
    }
    override fun toString() = "PrinterIccProfiles(${attributes.joinToString()})"
}
