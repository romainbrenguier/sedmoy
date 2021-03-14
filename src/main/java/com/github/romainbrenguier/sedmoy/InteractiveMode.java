package com.github.romainbrenguier.sedmoy;

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
  final List<Operation> operations = new ArrayList<>();

  public InteractiveMode(List<String> inputData) {
    this.inputData = inputData;
    this.scanner = new Scanner(inputStream);
  }

  private List<Method> choices() {
    return Arrays.asList(String.class.getDeclaredMethods());
  }

  private Operation inputOperation(Method method) {
    printStream.println("For method " + method.getName());
    final Class<?>[] parameterTypes = method.getParameterTypes();
    final Object[] parameters = new Object[parameterTypes.length];
    for (int i = 0; i < parameterTypes.length; ++i) {
      printStream.println("Enter parameter " + i + " of type " + parameterTypes[i].toString());
      final String line = scanner.nextLine();
      parameters[i] = line;
    }
    return new Operation(method, parameters);
  }

  final static String choiceCodes =
      ":<0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  final static int QUIT = -2;
  final static int CANCEL = -1;

  /**
   * @return -2 for quit, -1 to cancel last operation, index of the method otherwise
   */
  int chooseOperation(List<String> choices) {
    printStream.println("Choices:");
    printStream.println("q to stop");
    printStream.println("b to cancel last operation");
    for (int i = 0; i < choices.size(); ++i) {
      printStream.println(choiceCodes.charAt(i + 2) + " " + choices.get(i));
    }
    final String line = scanner.nextLine();
    return choiceCodes.indexOf(line.charAt(0)) - 2;
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
      printStream.println(choiceCodes.charAt(i + 2) + " " + Arrays
          .toString(matchingChoice.get(i).getParameterTypes()));
    }
    final String line = scanner.nextLine();
    return matchingChoice.get(choiceCodes.indexOf(line.charAt(0)) - 2);
  }

  /** return true for continue and false to stop */
  public boolean step() {
    printStream.println("Input data:");
    inputData.stream().limit(INPUT_PREVIEW_LENGTH).forEach(printStream::println);
    printStream.println("Current output:");
    applyOperations().stream().limit(INPUT_PREVIEW_LENGTH).forEach(printStream::println);
    final List<Method> methods = choices();
    final List<String> choiceNames =
        methods.stream().map(Method::getName).distinct().collect(Collectors.toList());
    int choice = chooseOperation(choiceNames);
    if (choice == QUIT) {
      return false;
    }
    if (choice == CANCEL) {
      operations.remove(operations.size() - 1);
      return true;
    }
    String choiceName = choiceNames.get(choice);
    printStream.println("You chose: " + choiceName);
    Method method = chooseMethodWithName(methods, choiceName);
    final Operation operation = inputOperation(method);
    operations.add(operation);
    List<Object> result = applyOperations();
    printStream.println("Result: ");
    result.forEach(printStream::println);
    return true;
  }

  private List<Object> applyOperations() {
    List<Object> result = new ArrayList<>(inputData);
    for (Operation value : operations) {
      result = result.stream().map(value::apply).collect(Collectors.toList());
    }
    return result;
  }

  public int run() {
    boolean stop = false;
    while (!stop) {
      stop = !step();
    }
    return 0;
  }
}
