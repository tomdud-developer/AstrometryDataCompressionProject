# GAIA Astrometry Data Compressors Comparison

Project obtain to compare different compressors used on GAIA astrometry data. Compressors has been applied for DR1 and DR3 GAIA data. DR1 and DR3 are represented in CSV files, DR1 contain about 300000 rows and 57 columns, weight is about 100MB. DR3 contain about 500000 rows and 152 columns, weight is about 600MB.

Compressors list:
- 7ZIP
- BSC
- GZIP
- M03
- MCM
- PPMD
- ZSTD

# Documentation
Application allow to define files for process. Single **process** can run multiple **sessions**. Each session can be
created with different files and different modifications. User can define files and modifications in **external.properties** file.
The main flow of the program is presented below. 
![Diagram.svg](assets%2FDiagram.svg)

## Session
The session has following stages:
1. Read original file
2. Make transformations
3. Compression by defined methods
4. Decompression
5. Make reversal transformations
6. Check integrity
7. Generate statistics

## Statistics
Each session generates statistics and sends them to presentation layer.
Session's statistics contains information about used compression methods and compression and decompression statistics.

### Compression statistics
- CompressionRatio (Compared to file **after** transformations)
- OutputSizeInMB 
- CompressionSpeedInMBPS 
- CompressionTimeInSeconds 
- General compression ratio (Compared to original file, **before** transformations)

### Decompression statistics
- DecompressedFileInMB
- DecompressionSpeedInMBPS
- DecompressionTimeInSeconds

### Transformations statistics
- modificationTimeInSeconds
- reversalTimeInSeconds
- checkEqualsTimeInSeconds


## Transformations
The program provides following transformations:
- TRANSPOSE
- TRANSFORM_BOOLEANS
- TRANSFORM_NOT_AVAILABLE
- TRANSFORM_SOLUTION_ID
- DESIGNATION_TRANSFORM
- TRANSFORM_REF_EPOCHS
- TRANSFORM_NULL

The Java program loads all cells from a file into memory, storing them in a common array, enabling efficient access and manipulation of the data. **Disadvantage of this solutions is increased memory consumption.**
```java
public class CSV implements Transpositionable, Saveable {
    private File file;
    private String[][] array;
    private int width;
    private int height;
    
    ...
}
```

### TRANSPOSE
It changes row with columns. That's helpful for compressions algorithms, because one row contains similar columns.
The CSV class implements Transpositionable interface. The transpose() method generate new CSV object.
```java
@Override
public CSV transpose() {
    CSV transposedCSV = new CSV();

    String[][] transposedArray = new String[width][height];

    for (int row = 0; row < height; row++)
        for (int col = 0; col < width; col++)
            transposedArray[col][row] = array[row][col];

    transposedCSV.setArray(transposedArray);
    transposedCSV.width = height;
    transposedCSV.height = width;
    transposedCSV.isVertically = !isVertically;

    return transposedCSV;
}
```

### TRANSFORM_BOOLEANS
In DR1 data there is a two columns with 'true' and 'false' values. In DR3 data there is about 14 columns. The transform function replace all 'true'
 and 'false' on binary 01 flag.

For DR1 data:
```java
@Override
public CSV revertTransformBoolean() {
    checkVerticality(transformedCSV);

    String[][] arr = transformedCSV.getArray();

    for (int i = 1; i < transformedCSV.getHeight(); i++) {
    if(arr[i][astrometric_primary_flag_column].equals("1"))
    arr[i][astrometric_primary_flag_column] = "true";
    else if(arr[i][astrometric_primary_flag_column].equals("0"))
    arr[i][astrometric_primary_flag_column] = "false";

    if(arr[i][duplicated_source_column].equals("1"))
    arr[i][duplicated_source_column] = "true";
    else if(arr[i][duplicated_source_column].equals("0"))
    arr[i][duplicated_source_column] = "false";
    }

    return transformedCSV;
}
```
### TRANSFORM_NOT_AVAILABLE
Any cell in DR1 and DR2 can contain "NOT_AVAILABLE" string, it is replace on '?' character

```java
@Override
public CSV transformNotAvailable() {
    checkVerticality(transformedCSV);

    String[][] arr = transformedCSV.getArray();

    for (int i = 1; i < transformedCSV.getHeight(); i++) {
        if(arr[i][phot_variable_flag_column].equals(not_available_string))
            arr[i][phot_variable_flag_column] = "?";
    }

    return transformedCSV;
}
```

### TRANSFORM_SOLUTION_ID
This is a unique ID for file and repeat in every row for **solution_id** column in DR1 and DR3. **solution_id** can be deleted for each row except first.
```java
@Override
public CSV transformID() {
        checkVerticality(transformedCSV);

        String[][] arr = transformedCSV.getArray();
        solutionID = arr[1][0];

        for (int i = 1; i < transformedCSV.getHeight(); i++) {
        arr[i][solution_id_column] = "";
        }

        return transformedCSV;
}
```

### DESIGNATION_TRANSFORM
In DR3, there is a designation column with format "Gaia DR3 4295806720". "Gaia DR3 " from this string can be deleted. 
```java
public CSV transformDesignation() {
    checkVerticality(transformedCSV);

    String[][] arr = transformedCSV.getArray();
    designationString = arr[1][designation_column].split(" ")[0] + " " + arr[1][designation_column].split(" ")[1];

    for (int i = 1; i < transformedCSV.getHeight(); i++) {
        arr[i][designation_column] = "";
    }

    return transformedCSV;
}
```
### TRANSFORM_GSPPHOT
The DR3 contains column **libname_gspphot**, which can contains "MARCS", "PHOENIX", "OB" or "null" strings, it is mapped by dictionary to short form.
```java
public CSV transformGspphotByDictionary() {
    checkVerticality(transformedCSV);

    String[][] arr = transformedCSV.getArray();

    for (int i = 1; i < transformedCSV.getHeight(); i++) {
        String str = gspphotDictionary.get(arr[i][libname_gspphot_column]);
        if(str != null) {
            arr[i][libname_gspphot_column] = str;
        }
    }

    return transformedCSV;
}
```
Dictionary:
```java
    gspphotDictionary = new HashMap<>();
    gspphotDictionary.put("\"MARCS\"", "M");
    gspphotDictionary.put("\"PHOENIX\"", "P");
    gspphotDictionary.put("\"OB\"", "O");

    gspphotDictionary.put("M", "\"MARCS\"");
    gspphotDictionary.put("P", "\"PHOENIX\"");
    gspphotDictionary.put("O", "\"OB\"");
```

### TRANSFORM_REF_EPOCHS
In DR1 and DR3 there is a **ref_epochs** column with a year of data collection, it is the same in each row. The rule is similar in TRANSFORM_SOLUTION_ID.
```java
@Override
public CSV transformRefEpochs() {
    checkVerticality(transformedCSV);

    String[][] arr = transformedCSV.getArray();
    refEpochs = arr[1][ref_epochs_column];

    for (int i = 1; i < transformedCSV.getHeight(); i++) {
        arr[i][ref_epochs_column] = "";
    }

    return transformedCSV;
}
```

### TRANSFORM_NULL
In DR3 there are a lot of "null" strings, which can be deleted and be empty. In average, per file there is a 22_000_000 "null" strings. Common file contains 80_000_00 cells. This is about 1/4 of all cells.

```java
public CSV transformNull() {
    checkVerticality(transformedCSV);

    String[][] arr = transformedCSV.getArray();

    for (int i = 1; i < transformedCSV.getHeight(); i++)
        for (int j = 0; j < transformedCSV.getWidth(); j++) {
            if(arr[i][j].equals("null"))
                arr[i][j] = "";
        }

    return transformedCSV;
}
```
____________________________
# Results

### Compression Statistics DR3 GAIA for using all transformations and transposition ###

| Compression Algorithm    | PPMD         | BSC          | GZIP         | MCM          | ZSTD         | SEVEN_Z      |
|-------------------------|--------------|--------------|--------------|--------------|--------------|--------------|
| CompressionRatio        | 3.288843074  | 3.141901834  | 2.635741022  | 3.581796524  | 2.518680151  | 3.045410099  |
| OutputSizeInMB          | 148.612007   | 155.56233    | 185.436113   | **136.457101**   | 194.05464    | 160.491216   |
| CompressionSpeedInMBPS  | 21.73072611  | 25.37483388  | 12.49176537  | 3.338762761  | **126.6392352**  | 1.337527649  |
| CompressionTimeInSeconds| 22.4917275   | 19.2616658   | 39.1267011   | 146.3900268  | **3.8594798**    | 365.4216572  |
| General compression ratio| 4.398091118  | 4.201590115  | 3.524713377  | **4.789850753**  | 3.368170676  | 4.072554027  |

### Decompression Statistics DR3 GAIA ###

| Decompression Algorithm   | PPMD         | BSC          | GZIP         | MCM          | ZSTD         | SEVEN_Z      |
|---------------------------|--------------|--------------|--------------|--------------|--------------|--------------|
| DecompressedFileInMB      | 488.76157    | 488.76157    | 488.76157    | 488.76157    | 488.76157    | 488.76157    |
| DecompressionSpeedInMBPS  | 6.858963052  | 13.39538258  | 25.3854268   | 0.92925591   | **37.28734186**  | 12.53633204  |
| DecompressionTimeInSeconds| 21.666833    | 11.6131308   | 7.3048255    | 146.8455563  | **5.2043034**    | 12.8020872   |

### DR1 GAIA ###
![TransformationsComparison.png](assets%2Fresults%2FTransformationsComparison.png)

### DR3 GAIA ###
![DRR3_TransformationsComparison.png](assets%2Fresults%2FDRR3_TransformationsComparison.png)


## Configuration external.properties
```
#This is a folder where sessions will be created, in this folder should be also files to compress
session.WorkingDirectoryPath=E:\\Workspace


#Available transformations: TRANSPOSE,TRANSFORM_BOOLEANS,TRANSFORM_NOT_AVAILABLE,TRANSFORM_SOLUTION_ID,TRANSFORM_REF_EPOCHS
session.fileNameToCompress[0]=GaiaSource_690706-690770.csv
session.fileToCompressModifiers[0]=TRANSFORM_DR3_ALL
session.fileNameToCompress[1]=GaiaSource_690706-690770.csv
session.fileToCompressModifiers[1]=TRANSPOSE
session.fileNameToCompress[2]=GaiaSource_690706-690770.csv
session.fileToCompressModifiers[2]=TRANSPOSE,TRANSFORM_DR3_ALL
session.fileNameToCompress[3]=GaiaSource_690706-690770.csv
session.fileToCompressModifiers[3]=

#Sessions are running one after another, but in session you can run all compressors parallel
session.isShouldBeParallelComputing=false

#Available compressors: PPMD,M03,BSC,GZIP,MCM,ZSTD,SEVEN_Z
session.methods=PPMD,BSC,GZIP,MCM,ZSTD,SEVEN_Z

#You should provide compressors executable files
compressors.directory=C:\\Users\\tomas\\IdeaProjects\\AstrometryDataCompressionProject\\Compressors

compressors.bsc.folderName=bsc
compressors.bsc.executableFileName=bsc.exe
compressors.bsc.defaultExtension=
compressors.bsc.compressCommand=e
compressors.bsc.options[0]=-b100

compressors.ppmd.folderName=ppmdi2
compressors.ppmd.executableFileName=PPMd.exe
compressors.ppmd.defaultExtension=ppmd
compressors.ppmd.compressCommand=e
compressors.ppmd.options[0]=-d

compressors.m03.folderName=m03
compressors.m03.executableFileName=M03.exe
compressors.m03.defaultExtension=
compressors.m03.compressCommand=e
compressors.m03.blockSize=1000

compressors.gzip.folderName=gzip
compressors.gzip.executableFileName=gzip.exe
compressors.gzip.defaultExtension=gz

compressors.mcm.folderName=mcm83
compressors.mcm.executableFileName=mcm.exe
compressors.mcm.defaultExtension=mcm
compressors.mcm.options[0]=-m5

compressors.zstd.folderName=zstd-v1.5.5-win64
compressors.zstd.executableFileName=zstd.exe
compressors.zstd.defaultExtension=zst
compressors.zstd.options[0]=-k
compressors.zstd.options[1]=-o

compressors.7z.folderName=xz-5.2.1-windows/bin_x86-64
compressors.7z.executableFileName=xz.exe
compressors.7z.defaultExtension=xz
compressors.7z.compressCommand=-z
compressors.7z.options[0]=
compressors.7z.options[1]=
```

## Authors
- [@tomadud-developer](https://www.github.com/tomadud-developer)


## License
[MIT](https://choosealicense.com/licenses/mit/)

