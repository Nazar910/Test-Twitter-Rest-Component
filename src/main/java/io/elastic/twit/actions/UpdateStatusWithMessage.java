package io.elastic.twit.actions;

import io.elastic.api.ExecutionParameters;
import io.elastic.api.Message;
import io.elastic.api.Module;
import io.elastic.twit.TwitterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;

/**
 * Created by pyvov on 03.03.2017.
 */
public class UpdateStatusWithMessage implements Module {
    private static final Logger logger = LoggerFactory.getLogger(UpdateStatusWithMessage.class);

    @Override
    public void execute(final ExecutionParameters parameters) {
        logger.info("About to update status");

        // incoming message
        final Message message = parameters.getMessage();

        // body contains the mapped data
        final JsonObject body = message.getBody();

        // contains action's configuration
        final JsonObject configuration = parameters.getConfiguration();

        // access the value of the mapped value into name field of the in-metadata
        final JsonString name = body.getJsonString("name");
        if (name == null) {
            throw new IllegalStateException("Name is required");
        }

        String text = TwitterUtils.updateStatus(name.getString(), configuration).getText();

        final JsonObject statusMessage = Json.createObjectBuilder().add("name", text).build();

        logger.info("Status successfully updated");

        final Message data
                = new Message.Builder().body(statusMessage).build();

        logger.info("Emitting data");

        // emitting the message to the platform
        parameters.getEventEmitter().emitData(data);

    }
}
