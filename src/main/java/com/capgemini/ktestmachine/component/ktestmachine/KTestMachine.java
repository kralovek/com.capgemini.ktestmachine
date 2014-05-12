package com.capgemini.ktestmachine.component.ktestmachine;

/**
 * Ce composant gère realisation des tests.
 */
public interface KTestMachine {
    /**
     * 
     * @param pSource source
     * @return 
     * true: tous les tests sont finis OK
     * false: au moins un test est fini KO
     */
    boolean test(final String pSource);
}
