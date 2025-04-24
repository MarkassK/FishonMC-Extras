package io.github.markassk.fishonmcextras.examples;

import io.github.markassk.fishonmcextras.examples.handler.ExampleHandler;

public class Example {
    // This example shows you how to access handlers

    public void exampleFunction() {
        /* Here is how we get the handler. It is always Handler.instance().
        *
        * You can get this instance many times and everywhere,
        * but it is always the same object across the project.
        *
        * It will initiate itself by doing instance().
        * So no need for "new ExampleHandler()"
        * */
        ExampleHandler exampleHandler = ExampleHandler.instance();

        // helloWorld was not exposed so we cannot access it. helloWorld is only used in the handler.
        //ExampleHandler.instance().helloWorld

        // shouldTick was exposed so we can access it.
        boolean exampleBoolean = exampleHandler.isShouldTick();

        // helloWorld was exposed to be able to set it.
        exampleHandler.setHelloWorld("Hello World");

        // otherFunction was exposed so we can access it.
        exampleHandler.otherFunction();
    }
}
