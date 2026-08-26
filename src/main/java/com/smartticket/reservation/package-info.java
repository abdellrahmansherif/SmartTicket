@org.springframework.modulith.ApplicationModule(
        allowedDependencies = {
                "inventory :: api",
                "event :: api",
                "identity :: api"
        }
)
package com.smartticket.reservation;