/**
 * Copyright 2013 International Business Machines Corp.
 * <p>
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.jbatch.tck.artifacts.specialized;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.AbstractCheckpointAlgorithm;
import jakarta.inject.Inject;

/*
 * Copyright 2012, 2020 International Business Machines Corp. and others
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


@jakarta.inject.Named("inventoryCheckpointAlgorithmNoOverride")
public class InventoryCheckpointAlgorithmNoOverride extends AbstractCheckpointAlgorithm {

    private static final String className = InventoryCheckpointAlgorithmNoOverride.class.getName();
    private static Logger logger = Logger.getLogger(InventoryCheckpointAlgorithmNoOverride.class.getPackage().getName());

    boolean inCheckpoint = false;
    int checkpointIterations;

    boolean init = false;

    @Inject
    @BatchProperty(name = "commitInterval")
    String commitIntervalString;
    int commitInterval;

    @Override
    public boolean isReadyToCheckpoint() throws Exception {
        String method = "isReadyToCheckpoint";
        if (logger.isLoggable(Level.FINER)) {
            logger.entering(className, method);
        }

        if (!init) {

            commitInterval = Integer.parseInt(commitIntervalString);
            checkpointIterations = 0;
            init = true;
        }

        checkpointIterations++;
        boolean ready = (checkpointIterations == commitInterval);

        if (ready) {
            checkpointIterations = 0;
        }

        return ready;
    }

}

