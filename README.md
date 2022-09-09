# sedmoy

Table interaction using groovy scripts.

Building: `gradle build`.
Example usage: `./sedmoy.sh -g scripts/example.groovy --input=src/test/resources/words.csv`

## SDM files

SDM files represent a list of tables with either raw data or a formula. They can be created using
raw data from a csv file:

`./sedmoy.sh --input=src/test/resources/words.csv  --output test.sdm`

Formula tables can then be added to that document:

`./sedmoy.sh --input=test.sdm --table-name "My formula" --number-of-lines 10 --add-formula "line + 10" --output test1.sdm`

The document can then be exported back to csv by evaluating the formulas:

`./sedmoy.sh --input=test1.sdm --output test1.csv`

# Formulas

Formulas are evaluated as groovy scripts which have the following variables 
predefined:
  
  * `line` and `column` indicates the position in the table of the cell currently being constructed
  * For each table defined in the document before the formula table, a variable with that name is defined.
  * `current` is the table currently being defined

The table variables have type `DataTable`, which define in particular the methods: `int height()`,
`int width()` and `String cell(int columnNumber, int lineNumber)`.

For example, here is to compute the total of all entries in the first column of the table named `table`:

`./sedmoy.sh --input=test1.sdm --add-formula "int t=0; for(int i=0;i<table.height();++i) t += Integer.parseInt(table.cell(0,i)); return t" --table-name "total"  --output test1.csv`

## Intellij plugin

To test the plugin, go to module parent and run: `gradle :plugin:runIde`.

It interprets groovy files and attempt to convert the value they compute as 
tables.
The plugin interprets groovy files with the special variables:
`currentDirectory` and `cachedFileReader`.