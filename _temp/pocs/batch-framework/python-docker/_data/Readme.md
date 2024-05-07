# Data
https://github.com/datablist/sample-csv-files

# Command to duplicate the file
(considering 1st record is a header)
```sh
$ tail -n +2 file.csv | cat file.csv - > file2X.csv
```