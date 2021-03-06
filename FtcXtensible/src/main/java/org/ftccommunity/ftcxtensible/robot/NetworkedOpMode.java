/*
 * Copyright © 2016 David Sargent
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM,OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ftccommunity.ftcxtensible.robot;

import org.ftccommunity.ftcxtensible.networking.http.RobotHttpServer;
import org.jetbrains.annotations.NotNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Networking components of the library; inherit this class to use the Networking components in
 * an {@link com.qualcomm.robotcore.eventloop.opmode.OpMode}
 *
 * @author David Sargent
 * @since 0.1
 * @see RobotContext#enableNetworking()
 */
public class NetworkedOpMode {
    private final Thread serverThread;

    /**
     * Create the networked version of an OpMode
     *
     * @param ctx a non-null {@link RobotContext}
     */
    protected NetworkedOpMode(@NotNull RobotContext ctx) {
        super();
        RobotHttpServer server = new RobotHttpServer(checkNotNull(ctx));
        serverThread = new Thread(server);
    }

    /**
     * Handles the server bootstrap and starts the HTTP server in a new thread, if the server hasn't
     * already been started
     */
    public void startServer() {
        if (!isAlive()) {
            serverThread.start();
        }
    }

    /**
     * Kills the HTTP server, if the server is alive
     */
    public void stopServer() {
        if (isAlive()) {
            serverThread.interrupt();
        }
    }

    /**
     * Checks if the server is running
     *
     * @return {@code true} if the server is running; otherwise {@code false}
     */
    public boolean isAlive() {
        return serverThread.isAlive();
    }
}
