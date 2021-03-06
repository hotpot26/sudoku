/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author yiwen
 */
public class Sudoku2 {
    private final static int NUMBER_OF_CELLS = 9;
    private final static int NUMBER_OF_CELLS_IN_SUBGRID = 3;
    private static int [][] grid = { 
        {8, 5, 6, 0, 1, 4, 7, 3, 0},
        {0, 9, 0, 0, 0, 0, 0, 4, 0},
        {2, 4, 0, 0, 0, 0, 1, 6, 0},
        {0, 6, 2, 0, 5, 9, 3, 0, 0},
        {0, 3, 1, 8, 0, 2, 4, 5, 0}, 
        {0, 0, 5, 3, 4, 0, 9, 2, 0},
        {0, 2, 4, 0, 0, 0, 0, 7, 3},
        {0, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 1, 8, 6, 3, 0, 2, 9, 4}
    };
    
    private static List<LinkedList<Cell>> rows = new ArrayList<LinkedList<Cell>>();
    private static List<LinkedList<Cell>> columns = new ArrayList<LinkedList<Cell>>();
    private static List<LinkedList<Cell>> subgrids = new ArrayList<LinkedList<Cell>>();

    private static int numberOfGivenCells = 0;
    private static int numberOfUnknownCells = 0;
    private static int previousNumberOfUnknownCells = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initializeCells();
        
        // find possible numbers based on cells in row
        for (int i = 0; i < 2; i++) {
            System.out.println("i = " + i);
            System.out.println("Find possible numbers based on cells in rows");
            for (LinkedList<Cell> row : rows) {
                fillPossibleNumbers(row);
            }
            System.out.println("Find possible numbers based on cells in columns");
            for (LinkedList<Cell> column : columns) {
                fillPossibleNumbers(column);
            }
            System.out.println("Find possible numbers based on cells in subgrids");
            for (LinkedList<Cell> sub : subgrids) {
                fillPossibleNumbers(sub);
            }

            printResult();
        }
        
        // if two cells has the same possible values, remove the values from
        // the rest of the dependant cells.
        
        // fillSoloNumbers();
    }
    
    private static void initializeCells() {
        // instantiate the rows that store the cells and columns that cross reference the cells
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            rows.add(i, new LinkedList<> ());
            columns.add(i, new LinkedList<> ());
            subgrids.add(i, new LinkedList<> ());
        }
        
        // initialize the cells within the grid.
        int subgridRow = 0;
        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            LinkedList<Cell> theRow = rows.get(i);
            if ((i/NUMBER_OF_CELLS_IN_SUBGRID > 0) && (i%NUMBER_OF_CELLS_IN_SUBGRID == 0)) {
                subgridRow ++;
            }
            for (int j = 0; j < NUMBER_OF_CELLS; j++) {
                Cell cell = new Cell (i, j, grid[i][j]);
                if (grid[i][j] == 0) {
                    numberOfGivenCells ++;
                }
                
                theRow.addLast(cell);
                
                LinkedList<Cell> theColumn = columns.get(j);
                theColumn.addLast(cell);
                
                int index = subgridRow*NUMBER_OF_CELLS_IN_SUBGRID + j/NUMBER_OF_CELLS_IN_SUBGRID;
                LinkedList<Cell> theSub = subgrids.get(index);
//                System.out.println("i = " + i + " j = " + j + " subgridRow" + subgridRow + " " + " subgrid" + index + " ");
                theSub.addLast(cell);
                
                // update the row and column for the cell in Cell for ease of reference
                cell.setRow(theRow);
                cell.setColumn(theColumn);
                cell.setSubgrid(theSub);
            }
        }
        
        System.out.println("==== rows:");
        for (LinkedList<Cell> i : rows) {
            System.out.println(i.toString());
        }
}

    private static void fillSoloNumbers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static void fillPossibleNumbers(LinkedList<Cell> line) {
        // build all known numbers within the line
        Set<Integer> existingNumbers = new HashSet<>();
        for (Cell cell : line) {
            if (cell.value != 0) {
                existingNumbers.add(cell.value);
            }
        }
        System.out.println("Existing numbers: " + existingNumbers.toString());
        
        // for every unknown cell, remove the known numbers 
        for (Cell cell : line) {
            if (cell.value == 0) {
                cell.removeMultipleValuesFromSet(existingNumbers);
            }
            System.out.println(cell.toString());
        }
    }

    private static void printResult() {
        System.out.println("Final Result:");
        int numberOfUnknown = 0;
        for (LinkedList <Cell> row : rows) {
            for (Cell c : row ) {
                System.out.print(c.value + ", ");
                if (c.value == 0) {
                    numberOfUnknown ++;
                }
            }
            System.out.println();
        }
        System.out.println("Given cells: " + numberOfGivenCells + ". There are still " + numberOfUnknown + " unsolved cells. The puzzle has outsmarted me!");
    }

    public static void print() {
        for (int[] i : grid) {
            for (int j : i) {
                System.out.print(j + ", ");
            }
            System.out.println();
        }
    } 
}