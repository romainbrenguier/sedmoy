package com.github.romainbrenguier.story;

import com.github.romainbrenguier.story.places.Room;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SceneTest {

    /**
     * Method under test: {@link Scene#make(Random)}
     */
    @Test
    void testMake() {
        // Arrange
        Random r = new Random(42L);

        // Act
        Scene actualMakeResult = Scene.make(r);

        // Assert
        List<TimedAction> timedActionList = actualMakeResult.actions;
        assertEquals(10, timedActionList.size());
        List<Character> characterList = actualMakeResult.characters;
        assertEquals(5, characterList.size());
        List<Room> roomList = actualMakeResult.place.rooms;
        assertEquals(7, roomList.size());
        Character getResult = characterList.get(4);
        assertFalse(getResult.isWoman);
        Action action = timedActionList.get(0).action;
        assertTrue(action instanceof Action.Move);
        Action action1 = timedActionList.get(7).action;
        assertTrue(action1 instanceof Action.Move);
        Action action2 = timedActionList.get(8).action;
        assertTrue(action2 instanceof Action.Move);
        assertEquals(Status.Scholar, getResult.status);
        Action action3 = timedActionList.get(9).action;
        assertTrue(action3 instanceof Action.Move);
        Action action4 = timedActionList.get(1).action;
        assertTrue(action4 instanceof Action.Move);
        Character getResult1 = characterList.get(1);
        assertEquals(Status.Criminal, getResult1.status);
        Character getResult2 = characterList.get(0);
        assertEquals(6, getResult2.age);
        assertTrue(getResult2.isWoman);
        assertEquals(Status.Scholar, getResult2.status);
        assertEquals(Integer.SIZE, getResult1.age);
        Character getResult3 = characterList.get(2);
        assertEquals(Status.Scholar, getResult3.status);
        assertFalse(getResult1.isWoman);
        Action action5 = timedActionList.get(2).action;
        assertTrue(action5 instanceof Action.Move);
        assertEquals(13, getResult3.age);
        Character getResult4 = characterList.get(3);
        assertEquals(Status.Homeless, getResult4.status);
        assertFalse(getResult3.isWoman);
        assertEquals(15, getResult.age);
        assertEquals(11, getResult4.age);
        assertTrue(getResult4.isWoman);
        assertSame(getResult4, ((Action.Move) action).character);
        Room getResult5 = roomList.get(0);
        assertSame(getResult5, ((Action.Move) action1).to);
        assertSame(getResult3, ((Action.Move) action5).character);
        assertSame(getResult2, ((Action.Move) action2).character);
        assertSame(getResult3, ((Action.Move) action3).character);
        assertSame(getResult4, ((Action.Move) action4).character);
        assertSame(getResult5, ((Action.Move) action4).from);
        assertSame(roomList.get(5), ((Action.Move) action4).to);
        Room getResult6 = roomList.get(1);
        assertSame(getResult6, ((Action.Move) action2).from);
        assertSame(roomList.get(6), ((Action.Move) action2).to);
        assertSame(getResult6, ((Action.Move) action).to);
        assertSame(getResult6, ((Action.Move) action5).to);
        assertSame(((Action.Move) action3).from, ((Action.Move) action3).to);
        assertSame(getResult2, ((Action.Move) action1).character);
        assertSame(getResult5, ((Action.Move) action).from);
    }

    /**
     * Method under test: {@link Scene#make(Random)}
     */
    @Test
    void testMake4() {
        // Arrange
        Random r = new Random(17L);

        // Act
        Scene actualMakeResult = Scene.make(r);

        // Assert
        List<TimedAction> timedActionList = actualMakeResult.actions;
        assertEquals(10, timedActionList.size());
        List<Character> characterList = actualMakeResult.characters;
        assertEquals(8, characterList.size());
        List<Room> roomList = actualMakeResult.place.rooms;
        assertEquals(11, roomList.size());
        Character getResult = characterList.get(6);
        assertEquals(Status.Prostitute, getResult.status);
        Action action = timedActionList.get(8).action;
        assertTrue(action instanceof Action.Move);
        Character getResult1 = characterList.get(7);
        assertEquals(77, getResult1.age);
        Character getResult2 = characterList.get(5);
        assertTrue(getResult2.isWoman);
        assertTrue(getResult1.isWoman);
        Action action1 = timedActionList.get(0).action;
        assertTrue(action1 instanceof Action.Move);
        Action action2 = timedActionList.get(7).action;
        assertTrue(action2 instanceof Action.Move);
        assertEquals(Status.Aristocrat, getResult1.status);
        assertEquals(69, getResult.age);
        assertFalse(getResult.isWoman);
        Action action3 = timedActionList.get(9).action;
        assertTrue(action3 instanceof Action.Move);
        Character getResult3 = characterList.get(1);
        assertEquals(Status.Scholar, getResult3.status);
        Action action4 = timedActionList.get(1).action;
        assertTrue(action4 instanceof Action.Move);
        Character getResult4 = characterList.get(0);
        assertEquals(36, getResult4.age);
        assertEquals(Status.Artisan, getResult4.status);
        assertTrue(getResult4.isWoman);
        assertEquals(Status.Entertainer, getResult2.status);
        Character getResult5 = characterList.get(2);
        assertEquals(Status.Politician, getResult5.status);
        assertEquals(39, getResult3.age);
        assertTrue(getResult3.isWoman);
        Action action5 = timedActionList.get(2).action;
        assertTrue(action5 instanceof Action.Move);
        assertEquals(51, getResult5.age);
        assertFalse(getResult5.isWoman);
        assertEquals(65, getResult2.age);
        assertSame(getResult5, ((Action.Move) action3).character);
        assertSame(getResult1, ((Action.Move) action1).character);
        assertSame(getResult5, ((Action.Move) action).character);
        assertSame(roomList.get(9), ((Action.Move) action3).to);
        assertSame(getResult3, ((Action.Move) action2).character);
        assertSame(roomList.get(1), ((Action.Move) action1).to);
        assertSame(roomList.get(10), ((Action.Move) action).to);
        assertSame(getResult4, ((Action.Move) action5).character);
        assertSame(getResult4, ((Action.Move) action4).character);
        assertSame(((Action.Move) action3).from, ((Action.Move) action4).to);
    }
}

