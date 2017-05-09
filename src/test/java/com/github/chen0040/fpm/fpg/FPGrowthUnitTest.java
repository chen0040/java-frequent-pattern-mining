package com.github.chen0040.fpm.fpg;


import com.github.chen0040.fpm.AssocRuleMiner;
import com.github.chen0040.fpm.apriori.AprioriUnitTest;
import com.github.chen0040.fpm.data.ItemSets;
import com.github.chen0040.fpm.data.MetaData;
import com.github.chen0040.fpm.utils.TupleTwo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 9/5/2017.
 */
public class FPGrowthUnitTest {


   private static final Logger logger = LoggerFactory.getLogger(FPGrowthUnitTest.class);

   @Test public void test_minePatterns() throws Exception {
      logger.info("Using FPGrowth");

      List<List<String>> database = createSimpleData();

      ItemSets fis = testBasic(database);

      fis.stream().forEach(itemSet -> logger.info("item-set: {}", itemSet));
   }

   @Test public void test_compareWithApriori() throws Exception {
      List<List<String>> database = createSimpleData();



      ItemSets fis1 = testBasic(database);
      ItemSets fis2 = new AprioriUnitTest().basicTest(database);

      assertThat(fis1.getSignature()).isEqualTo(fis2.getSignature());

      assertTrue(fis1.isSubsetOf(fis2));
      assertTrue(fis2.isSubsetOf(fis1));
   }


   private ItemSets testBasic(List<List<String>> database) {
      AssocRuleMiner method = new FPGrowth();
      method.setMinSupportLevel(2);

      MetaData metaData = new MetaData(database);

      return method.minePatterns(database, metaData.getUniqueItems());
   }


   @Test public void test_mineMaxPatterns() throws Exception {
      logger.info("Using FPGrowth for max patterns mining");

      List<List<String>> database = createSimpleData();

      ItemSets fis = testBasicMax(database);

      fis.stream().forEach(itemSet -> logger.info("max-item-set: {}", itemSet));
   }


   private ItemSets testBasicMax(List<List<String>> database) {
      AssocRuleMiner method = new FPGrowth();
      method.setMinSupportLevel(2);

      MetaData metaData = new MetaData(database);

      return method.findMaxPatterns(database, metaData.getUniqueItems());
   }

   public List<List<String>> createSimpleData(){
      List<List<String>> database = new ArrayList<>();

      database.add(Arrays.asList("f", "a", "c", "d", "g", "i", "m", "p"));
      database.add(Arrays.asList("a", "b", "c", "f", "l", "m", "o"));
      database.add(Arrays.asList("b", "f", "h", "j", "o", "w"));
      database.add(Arrays.asList("b", "c", "k", "s", "p"));
      database.add(Arrays.asList("a", "f", "c", "e", "l", "p", "m", "n"));

      database.forEach(transaction -> logger.info("transaction: {}", transaction.stream().collect(Collectors.joining(", "))));

      return database;
   }
}
