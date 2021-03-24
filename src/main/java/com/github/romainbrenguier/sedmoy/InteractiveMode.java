package com.github.romainbrenguier.sedmoy;

import com.github.romainbrenguier.sedmoy.operation.ConstantParameter;
import com.github.romainbrenguier.sedmoy.operation.MethodOperation;
import com.github.romainbrenguier.sedmoy.operation.Operation;
import com.github.romainbrenguier.sedmoy.operation.Operation.State;
import com.github.romainbrenguier.sedmoy.operation.Operations;
import com.github.romainbrenguier.sedmoy.operation.Parameter;
import com.github.romainbrenguier.sedmoy.operation.PopOperation;
import com.github.romainbrenguier.sedmoy.operation.PushOperation;
import com.github.romainbrenguier.sedmoy.operation.StackParameter;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InteractiveMode {
  final static int INPUT_PREVIEW_LENGTH = 10;
  final List<String> inputData;
  final InputStream inputStream = System.in;
  final PrintStream printStream = System.out;
  final Scanner scanner;
  final Operations operations = new Operations();

  public InteractiveMode(List<String> inputData) {
    this.inputData = inputData;
    this.scanner = new Scanner(inputStream);
  }

  public List<Method> choices(Class<?> forClass) {
    return Arrays.asList(forClass.getDeclaredMethods());
  }

  private MethodOperation inputOperation(Method method) {
    printStream.println("For method " + method.getName());
    final Class<?>[] parameterTypes = method.getParameterTypes();
    final List<Parameter> parameters = new ArrayList<>();
    for (int i = 0; i < parameterTypes.length; ++i) {
      printStream.println("Enter parameter " + i + " of type " + parameterTypes[i].toString());
      printStream.println("or \"-{index}\" to use an element from the stack");
      final String line = scanner.nextLine();
      if (line.charAt(0) == '-') {
        parameters.add(new StackParameter(Integer.parseInt(line.substring(1))));
      } else {
        parameters.add(new ConstantParameter(stringToObject(line, parameterTypes[i])));
      }
    }
    return new MethodOperation(method, parameters);
  }

  final static String specialCodes =  ":<+-";
  final static int QUIT = 0;
  final static int CANCEL = 1;
  final static int PUSH = 2;
  final static int POP = 3;
  final static String choiceCodes =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private static String stretchString(String s, int length) {
    StringBuilder builder = new StringBuilder(s);
    for (int i = s.length(); i < length; ++i) {
      builder.append(" ");
    }
    return builder.toString();
  }

  private static String shortenString(String message, int length) {
    if (message.length() < length) {
      return stretchString(message, length);
    }
    return message.substring(0, length - 3) + "...";
  }

  /**
   * @return index of the method otherwise, or 1000 + special code
   */
  int chooseOperation(List<String> choices) {
    printStream.println("Choices:");
    printChoice(specialCodes.charAt(QUIT), "stop");
    printChoice(specialCodes.charAt(CANCEL), "cancel last operation");
    printChoice(specialCodes.charAt(PUSH), "push to stack");
    printChoice(specialCodes.charAt(POP), "pop from stack");
    printStream.println();
    for (int i = 0; i < choices.size(); ++i) {
      printChoice(choiceCodes.charAt(i), choices.get(i));
      printStream.print((i % 3 == 2 || i == choices.size() - 1) ? "\n" : "");
    }
    final char choiceChar = scanner.nextLine().charAt(0);
    final int specialCode = specialCodes.indexOf(choiceChar);
    if (specialCode != -1) {
      return 1000 + specialCode;
    }
    return choiceCodes.indexOf(choiceChar);
  }

  void printChoice(char key, String description) {
    printStream.print("[" + key + "] " + stretchString(description, 31));
  }

  /**
   * @return -2 for quit, -1 to cancel last operation, index of the method otherwise
   */
  Method chooseMethodWithName(List<Method> methods, String name) {
    List<Method> matchingChoice = methods.stream()
        .filter(method -> method.getName().equals(name))
        .collect(Collectors.toList());
    if (matchingChoice.size() == 1) {
      return matchingChoice.get(0);
    }
    printStream.println("Choices:");
    for (int i = 0; i < matchingChoice.size(); ++i) {
      printChoice(
          choiceCodes.charAt(i),
          Arrays.toString(matchingChoice.get(i).getParameterTypes()));
      printStream.println();
    }
    final String line = scanner.nextLine();
    return matchingChoice.get(choiceCodes.indexOf(line.charAt(0)));
  }

  private void highlight(String message) {
    printStream.println("===== " + message + " =====");
  }

  private static String objectToString(Object object) {
    if (object.getClass().isArray()) {
      Object[] array = (Object[]) object;
      return String.format("[%s]",
          Arrays.stream(array).map(InteractiveMode::objectToString)
              .collect(Collectors.joining(", ")));
    }
    return object.toString();
  }

  private static Object stringToObject(String input, Class<?> target) {
    if (target.equals(Integer.class) || target.equals(int.class)) {
      return Integer.parseInt(input);
    }
    return input;
  }

  /**
   * One step of the main loop.
   * Return true for continue and false to stop
   */
  public boolean step() {
    highlight("Input data:");
    final List<Object> inputPreview =
        inputData.stream().limit(INPUT_PREVIEW_LENGTH).collect(Collectors.toList());
    inputPreview.forEach(printStream::println);
    highlight("Current output:");
    final List<Operation.State> currentOutput = operations.apply(inputPreview);
    currentOutput.stream()
        .map(InteractiveMode::stateToString)
        .forEach(printStream::println);
    final Class<?> classOfFirstResult = currentOutput.get(0).data.getClass();
    final List<Method> methods = choices(classOfFirstResult);
    final List<String> choiceNames =
        methods.stream().map(Method::getName).distinct().collect(Collectors.toList());
    int choice = chooseOperation(choiceNames);
    if (choice >= 1000) {
      return handleSpecialCode(choice - 1000);
    }
    String choiceName = choiceNames.get(choice);
    printStream.println("You chose: " + choiceName);
    Method method = chooseMethodWithName(methods, choiceName);
    final MethodOperation operation = inputOperation(method);
    operations.add(operation);
    return true;
  }

  private static String stateToString(Operation.State state) {
    final String stackString = state.stack.stream()
        .map(obj -> shortenString(objectToString(obj), 10))
        .collect(Collectors.joining(","));
    return "{" + stackString + "} " + objectToString(state.data);
  }

  /**
   * @param choice code of a special choice
   * @return true for continue, false for stop execution
   */
  public boolean handleSpecialCode(int choice) {
    if (choice == QUIT) {
      List<State> result = operations.apply(new ArrayList<>(inputData));
      highlight("Result: ");
      result.stream().map(state -> state.data)
          .map(InteractiveMode::objectToString).forEach(printStream::println);
      return false;
    }
    if (choice == CANCEL) {
      operations.removeLast();
    }
    if (choice == PUSH) {
      operations.add(new PushOperation());
    }
    if (choice == POP) {
      operations.add(new PopOperation());
    }
    return true;
  }

  public int run() {
    boolean stop = false;
    while (!stop) {
      stop = !step();
    }
    return 0;
  }
}
