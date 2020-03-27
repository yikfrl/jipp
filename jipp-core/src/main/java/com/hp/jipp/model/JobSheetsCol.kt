// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
//
// DO NOT MODIFY. Code is auto-generated by genTypes.py. Content taken from registry at
// https://www.iana.org/assignments/ipp-registrations/ipp-registrations.xml, updated on 2020-02-20
@file:Suppress("MaxLineLength", "WildcardImport")

package com.hp.jipp.model

import com.hp.jipp.encoding.* // ktlint-disable no-wildcard-imports

/**
 * Data object corresponding to a "job-sheets-col" collection as defined in:
 * [PWG5100.7](https://ftp.pwg.org/pub/pwg/candidates/cs-ippjobext20-20190816-5100.7.pdf).
 */
@Suppress("RedundantCompanionReference", "unused")
data class JobSheetsCol
constructor(
    /** May contain any keyword from [JobSheet] or a name. */
    var jobSheets: KeywordOrName? = null,
    /** May contain any keyword from [Media] or a name. */
    var media: KeywordOrName? = null,
    var mediaCol: MediaCol? = null
) : AttributeCollection {

    /** Construct an empty [JobSheetsCol]. */
    constructor() : this(null, null, null)

    /** Produce an attribute list from members. */
    override val attributes: List<Attribute<*>> by lazy {
        listOfNotNull(
            jobSheets?.let { Types.jobSheets.of(it) },
            media?.let { Types.media.of(it) },
            mediaCol?.let { Types.mediaCol.of(it) }
        )
    }

    /** Type for attributes of this collection */
    class Type(override val name: String) : AttributeCollection.Type<JobSheetsCol>(JobSheetsCol)

    /** All member names as strings. */
    object Name {
        /** "job-sheets" member name */
        const val jobSheets = "job-sheets"
        /** "media" member name */
        const val media = "media"
        /** "media-col" member name */
        const val mediaCol = "media-col"
    }

    /** Types for each member attribute. */
    object Types {
        val jobSheets = KeywordOrNameType(Name.jobSheets)
        val media = KeywordOrNameType(Name.media)
        val mediaCol = MediaCol.Type(Name.mediaCol)
    }

    /** Defines types for each member of [JobSheetsCol] */
    companion object : AttributeCollection.Converter<JobSheetsCol> {
        override fun convert(attributes: List<Attribute<*>>): JobSheetsCol =
            JobSheetsCol(
                extractOne(attributes, Types.jobSheets),
                extractOne(attributes, Types.media),
                extractOne(attributes, Types.mediaCol)
            )
    }
    override fun toString() = "JobSheetsCol(${attributes.joinToString()})"
}
