
/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.server.remote.rest.prometheus;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("metrics")
public class MetricsResource {

    private static final Logger logger = LoggerFactory.getLogger(MetricsResource.class);
    public static CollectorRegistry prometheusRegistry = CollectorRegistry.defaultRegistry;

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public Response getModels() {
        logger.trace("Prometheus is scraping");

        Enumeration<Collector.MetricFamilySamples> mfs = prometheusRegistry.metricFamilySamples();

        StreamingOutput stream = os -> {
            Writer writer = new BufferedWriter(new OutputStreamWriter(os));
            TextFormat.write004(writer, mfs);
            writer.flush();
        };

        return Response.ok(stream).build();

    }
}
