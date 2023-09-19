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
- TRANSFORM_REF_EPOCHS

The Java program loads all cells from a file into memory, storing them in a common array, enabling efficient access and manipulation of the data.
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

