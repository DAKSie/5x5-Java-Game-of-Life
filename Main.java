import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Main {
	//clears console
	public static void clearConsole() {
    try {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        }
    } catch (Exception e) {
        System.err.println("Error clearing console: " + e.getMessage());
    }
}
	
	//Handles delays
	public static void wait(int ms) {
    try {
        Thread.sleep(ms);
    } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
    }
}

    //Function to print grid
    public static void startSimulation(int[][] mainGrid, int gridSize) {
        for (int[] row : mainGrid) {
            for (int cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
		int generations = 0;
		while (generations < 1000) { // Run for 1000 generations
            //wait(1000); //add this to add a 1 second delay for each generation
			clearConsole();
			nextGeneration(mainGrid, gridSize);
			generations++;
		}
		System.out.println("Generations: " + generations);
    }

    //Update cell
    public static void updateCell(int[][] mainGrid, int x, int y) {
        // Check if the coordinates are within the grid bounds
        if (x >= 0 && x < mainGrid.length && y >= 0 && y < mainGrid[0].length) {
            mainGrid[x][y] = 1; // Mark the cell as live
        }
    }
	
	//Handles cell state logic
	public static boolean isCellAlive(int[][] mainGrid, int x, int y) {		
        if (x >= 0 && x < mainGrid.length && y >= 0 && y < mainGrid[0].length) {
            return mainGrid[x][y] == 1;
        }
        return false;
    }
	
	//Checks the live cells neighboring each cell
	public static int countLiveNeighbors(int[][] mainGrid, int x, int y) {
        int count = 0;
		
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
				//if the index is checking itself, skip;
                if (i == 0 && j == 0) continue;
                if (isCellAlive(mainGrid, x + i, y + j)) {
                    count++;
                }
            }
        }
        return count;
    }
	
	//handles printing of the grid
	public static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
	
	//Applies Game of life rules
	public static void nextGeneration(int[][] mainGrid, int gridSize) {
		int[][] nextGrid = new int[gridSize][gridSize];
	
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				int liveNeighbors = countLiveNeighbors(mainGrid, i, j);
				boolean alive = isCellAlive(mainGrid, i, j);
	
				if (alive) {
					if (liveNeighbors == 2 || liveNeighbors == 3) {
						nextGrid[i][j] = 1;
					} else {
						nextGrid[i][j] = 0;
					}
				} else {
					if (liveNeighbors == 3) {
						nextGrid[i][j] = 1;
					} else {
						nextGrid[i][j] = 0;
					}
				}
			}
		}
	
		// Copy the nextGrid to mainGrid
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				mainGrid[i][j] = nextGrid[i][j];
			}
		}
	
		printGrid(mainGrid);
	}

    public static void main(String[] args) {
		
        Scanner in = new Scanner(System.in);

        
        int choose = 1;
        int liveCellIndex = 0;
		int size = 5;

        String[] livePosString;
        ArrayList<Integer> livePos = new ArrayList<>();

        int[][] grid = {
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0}
        };

        while (choose != 3) {
            try {
                    System.out.print("1: Enter live cell\n2: Start Simulation\n3: End Game\nChoose an option: ");
                choose = in.nextInt();
                in.nextLine(); // Consume newline

                switch (choose) {
                    case 1:
                        // Input live cell positions
                        System.out.print("Enter Cell Index (n, n): ");
                        livePosString = in.nextLine().split(",");

                        if (Integer.parseInt(livePosString[0].trim()) <= 5 && Integer.parseInt(livePosString[1].trim()) <= 5) {
                            livePos.add(Integer.parseInt(livePosString[0].trim()));
                            livePos.add(Integer.parseInt(livePosString[1].trim()));
                            System.out.println("Live Cell Positions: " + livePos);

                            // Update the grid with the live cell positions
                            updateCell(grid, livePos.get(liveCellIndex), livePos.get(liveCellIndex + 1));
                            liveCellIndex += 2; // Move to the next pair of coordinates
                        } else {
                            System.out.println("Index must not be over 5");
                        }

                        break;
                    case 2:
                        startSimulation(grid, size);
                        break;
                    case 3:
                        System.out.println("Ending Game...");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                in.next(); // Clear the invalid input
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                break;
            }
        }

        in.close();
    }
}
