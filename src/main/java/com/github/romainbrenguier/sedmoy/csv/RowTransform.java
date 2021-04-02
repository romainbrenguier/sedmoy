package com.github.romainbrenguier.sedmoy.csv;

import com.github.romainbrenguier.sedmoy.csv.Row;

interface RowTransform {
  Row transform(Row input);
}
