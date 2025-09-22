package com.StupidRat.SysLvl;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;

/**
 * Robolectric test runner that tolerates missing NFC beam metadata in legacy android-all builds.
 */
public class LenientRobolectricTestRunner extends RobolectricTestRunner {
    public LenientRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected void finallyAfterTest(FrameworkMethod method) {
        try {
            super.finallyAfterTest(method);
        } catch (RuntimeException runtimeException) {
            if (runtimeException.getCause() instanceof NoSuchFieldException
                    && "sHasBeamFeature".equals(runtimeException.getCause().getMessage())) {
                return;
            }
            throw runtimeException;
        } catch (Throwable throwable) {
            if (throwable instanceof NoSuchFieldException
                    && "sHasBeamFeature".equals(throwable.getMessage())) {
                return;
            }
            throw new RuntimeException(throwable);
        }
    }
}
