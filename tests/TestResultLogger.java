package tests;

import org.junit.jupiter.api.extension.*;

public class TestResultLogger implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println(context.getDisplayName() + " PASSED");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println(context.getDisplayName() + " FAILED: " + cause.getMessage());
    }
}
