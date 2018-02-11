package com.github.examples;

import com.github.examples.tests.NewsSearchServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
       NewsSearchServiceTest.class
})
public class IntegrationSuite {
}
