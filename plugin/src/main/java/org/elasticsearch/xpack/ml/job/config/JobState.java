/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.ml.job.config;

import org.elasticsearch.Version;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

/**
 * Jobs whether running or complete are in one of these states.
 * When a job is created it is initialised in to the state closed
 * i.e. it is not running.
 */
public enum JobState implements Writeable {

    CLOSING, CLOSED, OPENED, FAILED, OPENING;

    public static JobState fromString(String name) {
        return valueOf(name.trim().toUpperCase(Locale.ROOT));
    }

    public static JobState fromStream(StreamInput in) throws IOException {
        return in.readEnum(JobState.class);
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        JobState state = this;
        // Pre v5.5 the OPENING state didn't exist
        if (this == OPENING && out.getVersion().before(Version.V_5_5_0)) {
            state = CLOSED;
        }
        out.writeEnum(state);
    }

    public String value() {
        return name().toLowerCase(Locale.ROOT);
    }


    /**
     * @return {@code true} if state matches any of the given {@code candidates}
     */
    public boolean isAnyOf(JobState... candidates) {
        return Arrays.stream(candidates).anyMatch(candidate -> this == candidate);
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
