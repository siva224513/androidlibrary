/*
 * Copyright (C) 2016 University of Washington
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opendatakit.common.android.utilities;

import android.os.Parcel;
import android.test.AndroidTestCase;

import org.opendatakit.common.desktop.WebLoggerDesktopFactoryImpl;
import org.opendatakit.database.service.OdkDbRow;
import org.opendatakit.database.service.OdkDbTable;

import java.io.IOException;

public class OdkDbTableTest extends AndroidTestCase {

  /* Test data */
  private static final String COLUMN1 = "firstCol";
  private static final String COLUMN2 = "secondCol";
  private static final String COLUMN3 = "thirdCol";
  private static final String COLUMN4 = "fourthCol";
  private static final String COLUMN5 = "fifthCol";
  private static final int TABLE_WIDTH = 5;

  private static final String ROW1COL1 = "10";
  private static final String ROW1COL2 = null;
  private static final String ROW1COL3 = "text value";
  private static final String ROW1COL4 = null;
  private static final String ROW1COL5 = "15";

  private static final String ROW2COL1 = "20";
  private static final String ROW2COL2 = null;
  private static final String ROW2COL3 = "10";
  private static final String ROW2COL4 = "test value";
  private static final String ROW2COL5 = "10";

  private static final int NUM_ROWS = 2;

  private static final String[] PRIMARY_KEY = {COLUMN1, COLUMN3};
  private static final String[] ROW1_PK = {ROW1COL1, ROW1COL3};
  private static final String[] ROW2_PK = {ROW2COL1, ROW2COL3};

  private static final String SQLCMD = "";
  private static final String[] BINDARGS = {"test", "text"};
  private static final String[] ORDERBYELEM = null;
  private static final String[] ORDERBYDIR = null;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    StaticStateManipulator.get().reset();
    WebLogger.setFactory(new WebLoggerDesktopFactoryImpl());
  }

  public void testOdkDbTableParcelation() throws IOException {

    /*
     * Create test data
     */
    String[] elementKeyForIndex = new String[TABLE_WIDTH];
    String[] rowValues1 = new String[TABLE_WIDTH];
    String[] rowValues2 = new String[TABLE_WIDTH];

    int index = 0;
    elementKeyForIndex[index] = COLUMN1;
    rowValues1[index] = ROW1COL1;
    rowValues2[index] = ROW2COL1;
    index++;

    elementKeyForIndex[index] = COLUMN2;
    rowValues1[index] = ROW1COL2;
    rowValues2[index] = ROW2COL2;
    index++;

    elementKeyForIndex[index] = COLUMN3;
    rowValues1[index] = ROW1COL3;
    rowValues2[index] = ROW2COL3;
    index++;

    elementKeyForIndex[index] = COLUMN4;
    rowValues1[index] = ROW1COL4;
    rowValues2[index] = ROW2COL4;
    index++;

    elementKeyForIndex[index] = COLUMN5;
    rowValues1[index] = ROW1COL5;
    rowValues2[index] = ROW2COL5;
    index++;

    String sqlCmd = SQLCMD;
    String[] bindArgs = BINDARGS;
    String[] orderByArgs = ORDERBYELEM;
    String[] orderByDirections = ORDERBYDIR;
    String[] primaryKey = PRIMARY_KEY;

    OdkDbTable table = new OdkDbTable(sqlCmd, bindArgs, orderByArgs, orderByDirections,
        primaryKey, elementKeyForIndex, NUM_ROWS);

    OdkDbRow row1 = new OdkDbRow(rowValues1, table);
    table.addRow(row1);

    OdkDbRow row2 = new OdkDbRow(rowValues2, table);
    table.addRow(row2);

    /*
     * Marshall the test data
     */
    Parcel p = Parcel.obtain();
    table.writeToParcel(p, 0);
    byte[] bytes = p.marshall();
    p.recycle();

    p = Parcel.obtain();
    p.unmarshall(bytes, 0, bytes.length);
    p.setDataPosition(0);

    OdkDbTable t = OdkDbTable.CREATOR.createFromParcel(p);

    /*
     * Test that the data survived
     */

    // sql command
    assertEquals(table.getSqlCommand(), t.getSqlCommand());

    // selectionArgs
    String[] sa = table.getSqlSelectionArgs();
    String[] sb = t.getSqlSelectionArgs();
    if ( sa != null && sb != null ) {
      assertEquals(sa.length, sb.length);
      for ( int i = 0 ; i < sa.length ; ++i ) {
        assertEquals(sa[i], sb[i]);
      }
    } else {
      assertNull(sa);
      assertNull(sb);
    }

    // order by elementKey
    String[] ea = table.getOrderByElementKeys();
    String[] eb = t.getOrderByElementKeys();
    if ( ea != null && eb != null ) {
      assertEquals(ea.length, eb.length);
      for ( index = 0 ; index < ea.length ; ++index ) {
        assertEquals(ea[index], eb[index]);
      }
    } else {
      assertNull(ea);
      assertNull(eb);
    }

    // order by direction
    String[] da = table.getOrderByDirections();
    String[] db = t.getOrderByDirections();
    if ( da != null && db != null ) {
      assertEquals(da.length, db.length);
      for ( index = 0 ; index < da.length ; ++index ) {
        assertEquals(da[index], db[index]);
      }
    } else {
      assertNull(da);
      assertNull(db);
    }

    // primary key
    String[] pa = table.getPrimaryKey();
    String[] pb = t.getPrimaryKey();
    if ( pa != null && pb != null ) {
      assertEquals(pa.length, pb.length);
      for ( index = 0 ; index < pa.length ; ++index ) {
        assertEquals(pa[index], pb[index]);
      }
    } else {
      assertNull(pa);
      assertNull(pb);
    }

    // verify rows
    assertEquals(NUM_ROWS, table.getNumberOfRows());
    assertEquals(NUM_ROWS, t.getNumberOfRows());

    OdkDbRow rat1 = table.getRowAtIndex(0);
    OdkDbRow rat2 = t.getRowAtIndex(0);
    OdkDbRow rbt1 = table.getRowAtIndex(1);
    OdkDbRow rbt2 = t.getRowAtIndex(1);

    for (int j = 0; j < TABLE_WIDTH; j++) {
      assertEquals(rat1.getDataByIndex(j), rat2.getDataByIndex(j));
      assertEquals(rbt2.getDataByIndex(j), rbt1.getDataByIndex(j));
    }
  }

}
