/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unicrush.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author szalontaijordan
 */
public class CandyCrushGameTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CandyCrushGameTest.class);
    
    private CandyCrushGame game;
    
    public CandyCrushGameTest() {
        this.game = new CandyCrushGame();
    }
    
    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("Testing class CandyCrushGame");
        // setup DB connection
    }
    
    @AfterClass
    public static void tearDownClass() {
        // close DB connection
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of initLevels method, of class CandyCrushGame.
     */
    @Test
    public void testInitLevels() {
        // TODO implement JPA, then create individual level instaces with
        //      the builer, and test some of them
    }
    
}
