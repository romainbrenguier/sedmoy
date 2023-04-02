package com.github.romainbrenguier.story.places;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.expression.discrete.relational.ReExpression;
import org.chocosolver.solver.variables.IntVar;

import javax.annotation.Nullable;
import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class RoomPlacer {
    public static Map<Integer, Point> placeRooms(Place place) {
        Map<Integer, Point> coordinates = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        final Integer entrance = place.entrances.get(0);
        queue.add(entrance);
        visited.add(entrance);
        Point origin = new Point(0, 0);
        coordinates.put(entrance, origin);
        int distance = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            int firstAvailable = 0;
            for (int i = 0; i < size; i++) {
                int currentRoom = queue.poll();
                for (int neighbor : place.connectedFrom(currentRoom)) {
                    if (!visited.contains(neighbor)) {
                        final int newY = Math.max(i, firstAvailable);
                        coordinates.put(neighbor, new Point(distance, newY));
                        firstAvailable = newY + 1;
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
            distance++;
        }
        return coordinates;
    }

    public static Integer[][] placeInTable(Map<Integer, Point> coordinates) {
        int sizeX = coordinates.values().stream()
                .map(p -> (int) p.getX()).reduce(Integer::max).orElse(0) + 1;
        int sizeY = coordinates.values().stream()
                .map(p -> (int) p.getY()).reduce(Integer::max).orElse(0) + 1;
        final Integer[][] table = new Integer[sizeX][sizeY];
        for (int i = 0; i < table.length; ++i)
            for (int j = 0; j < table[i].length; ++j)
                table[i][j] = -1;
        coordinates.entrySet().forEach(entry ->
                table[(int) entry.getValue().getX()][(int) entry.getValue().getY()] = entry.getKey());
        return table;
    }

    public static String formatTableMap(
            Integer[][] table,
            Function<Integer, List<Integer>> connections,
            Function<Integer, String> cellFormat,
            List<Integer> entrances) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < table.length; ++i) {
            String offset = " ".repeat(10).repeat(i);
            builder.append(offset);
            for (int j = 0; j < table[i].length; ++j) {
                if (table[i][j] == -1) builder.append(" ".repeat(20));
                else {
                    final boolean hasLeftDoor = i > 0 && connections.apply(table[i][j]).contains(table[i - 1][j]);
                    final boolean hasRightDoor = i > 0 && j < table[i - 1].length - 1 && connections.apply(table[i][j]).contains(table[i - 1][j + 1]);
                    builder.append(formatWall(hasLeftDoor, hasRightDoor, entrances.contains(table[i][j])));
                }
            }
            builder.append("\n");
            final Integer[] currentRow = table[i];
            builder.append(offset)
                    .append(formatVerticalWall(currentRow, j -> "", j -> false))
                    .append("\n");
            builder.append(offset)
                    .append(formatVerticalWall(currentRow,
                            j -> currentRow[j] != -1 ? cellFormat.apply(currentRow[j]) : "",
                            j -> j < currentRow.length - 1 && connections.apply(currentRow[j]).contains(currentRow[j + 1])))
                    .append("\n");
            builder.append(offset)
                    .append(formatVerticalWall(currentRow, j -> "", j -> false)).append("\n");
            builder.append(offset);
            for (int j = 0; j < table[i].length; ++j) {
                if (table[i][j] == -1) builder.append(" ".repeat(20));
                else {
                    final boolean hasLeftDoor =
                            i < table.length - 1 && j > 0 &&
                                    connections.apply(table[i][j]).contains(table[i + 1][j - 1]);
                    final boolean hasRightDoor = i < table.length - 1
                            && connections.apply(table[i][j]).contains(table[i + 1][j]);
                    builder.append(formatWall(hasLeftDoor, hasRightDoor, false));
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private static String formatVerticalWall(
            Integer[] roomArray,
            Function<Integer, String> text,
            Predicate<Integer> doorRight) {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < roomArray.length; ++j) {
            if (roomArray[j] == -1) builder.append(" ".repeat(20));
            else {
                if (j > 0 && doorRight.test(j - 1)) builder.append("_");
                else builder.append("|");
                builder.append(String.format(" %16s ", text.apply(j)));
                if (doorRight.test(j)) builder.append("_");
                else builder.append("|");
            }
        }
        return builder.toString();
    }

    private static String formatWall(boolean leftDoor, boolean rightDoor, boolean mainEntrance) {
        StringBuilder builder = new StringBuilder();
        builder.append("+").append("-".repeat(3));
        if (leftDoor) {
            builder.append(" ");
        } else {
            builder.append("-");
        }
        builder.append("-".repeat(3));
        if (mainEntrance) builder.append("[ ]");
        else builder.append("-".repeat(3));
        builder.append("-".repeat(3));
        if (rightDoor) {
            builder.append(" ");
        } else {
            builder.append("-");
        }
        builder.append("-".repeat(4)).append("+");
        return builder.toString();
    }

    public static class CoordinateVar {
        final IntVar varX;
        final IntVar varY;

        public CoordinateVar(IntVar varX, IntVar varY) {
            this.varX = varX;
            this.varY = varY;
        }

        public static CoordinateVar make(Model model, String prefix) {
            return new CoordinateVar(model.intVar(prefix + "x", 0, 5),
                    model.intVar(prefix + "y", 0, 5));
        }

        public ReExpression ne(CoordinateVar other) {
            return varX.ne(other.varX).or(varY.ne(other.varY));
        }

        public ReExpression hexNeighboor(CoordinateVar other) {
            // y = y' && x' = x +- 1 || y' = y-1 && x' in { x, x+1} || y' = y+1 && x' in {x-1, x}
            return other.varY.eq(varY).and(other.varX.dist(varX).eq(1))
                    .or(other.varY.add(1).eq(varY)
                            .and(other.varX.eq(varX).or(other.varX.eq(varX.add(1)))))
                    .or(other.varY.eq(varY.add(1))
                            .and(other.varX.eq(varX).or(other.varX.add(1).eq(varX))));
        }

        public Point get(Solution solution) {
            return new Point(solution.getIntVal(varX), solution.getIntVal(varY));
        }
    }

    @Nullable
    public static Map<Integer, Point> solveConstraints(Place place, int limit) {
        final Model model = new Model("Room placement");
        final Map<Integer, CoordinateVar> roomVar = new HashMap<>();
        for (int i = 0; i < place.rooms.size(); ++i) {
            roomVar.put(i, CoordinateVar.make(model, "Room" + i));
        }
        // All rooms have different coordinates
        for (int roomIndex = 0; roomIndex < place.rooms.size(); ++roomIndex) {
            for (int otherRoom = roomIndex + 1; otherRoom < place.rooms.size(); ++otherRoom) {
                roomVar.get(roomIndex).ne(roomVar.get(otherRoom)).post();
            }
        }

        // Entrance constraint
        for (int roomIndex : place.entrances) {
            roomVar.get(roomIndex).varX.eq(0).post();
        }

        // Neighboor constraints
        for (int roomIndex = 0; roomIndex < limit && roomIndex < place.rooms.size(); ++roomIndex) {
            for (int otherRoom : place.connectedFrom(roomIndex))
                if (otherRoom > roomIndex)
                    roomVar.get(roomIndex).hexNeighboor(roomVar.get(otherRoom)).post();
        }

        // Get solution
        Solution solution = model.getSolver().findSolution();
        if (solution == null) return null;

        Map<Integer, Point> coordinates = new HashMap<>();
        for (int roomIndex = 0; roomIndex < limit && roomIndex < place.rooms.size(); ++roomIndex) {
            coordinates.put(roomIndex, roomVar.get(roomIndex).get(solution));
        }
        return coordinates;
    }

    public static String makeMap(Place place) {
        return RoomPlacer.formatTableMap(
                RoomPlacer.placeInTable(RoomPlacer.solveConstraints(place, place.rooms.size())),
                place::connectedFrom,
                place.roomFormatter(), place.entrances);
    }
}

