/*
 * Copyright (c) 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.openjdk.bench.vm.compiler;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * Tests transformation that converts "~x + c" into "(c - 1) - x" in
 * AddNode::IdealIL.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 3)
public class AddIdealNotXPlusC {

    private static final int I_C = 1234567;

    private static final long L_C = 123_456_789_123_456L;

    private int iFld = 4711;

    private long lFld = 4711 * 4711 * 4711;

    @Benchmark
    public int baselineInt() {
        return iFld;
    }

    @Benchmark
    public long baselineLong() {
        return lFld;
    }

    @Benchmark
    public int testInt() {
        return helper(iFld);
    }

    @Benchmark
    public long testLong() {
        return helper(lFld);
    }

    // Convert "~x + c" into "(c - 1) - x" for int.
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private static int helper(int x) {
        return ~x + I_C + x;
    }

    // Convert "~x + c" into "(c - 1) - x" for long.
    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    private static long helper(long x) {
        return ~x + L_C + x;
    }
}
