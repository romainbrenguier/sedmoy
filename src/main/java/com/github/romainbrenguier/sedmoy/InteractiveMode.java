package com.github.romainbrenguier.sedmoy;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
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

  public int run() {
    printStream.println("Input data:");
    inputData.stream().limit(INPUT_PREVIEW_LENGTH).forEach(printStream::println);
    printStream.println("Choices:");
    final List<Method> methods = choices();
    final List<String> choiceNames =
        methods.stream().map(Method::getName).distinct().collect(Collectors.toList());
    for (int i = 0; i < choiceNames.size(); ++i) {
      printStream.println(i + " " + choiceNames.get(i));
    }
    final String line = scanner.nextLine();
    int choice = Integer.parseInt(line);
    printStream.println("You chose: " + choice);
    List<Method> matchingChoice = methods.stream()
        .filter(method -> method.getName().equals(choiceNames.get(choice)))
        .collect(Collectors.toList());
    final Operation operation = inputOperation(matchingChoice.get(0));
    final List<Object> result =
        inputData.stream().map(operation::apply).collect(Collectors.toList());
    printStream.println("Result: ");
    result.forEach(printStream::println);
    return choice;
  }

}
