package org.george;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.george.views.RegisterView;

public class SWApp {

    public static final Logger LOGGER_SWAPP = LogManager.getLogger(SWApp.class);

    public static void main(String[] args) {
        LOGGER_SWAPP.info("Routine started.");
        new RegisterView().show();

    }
}
