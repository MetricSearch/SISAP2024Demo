# Sisap 2024 Polyadic Query Demo Paper

## Online Version
The interface may be accessed at https://similarity.cs.st-andrews.ac.uk/polydemo/

## Running the Server
This git repository contains all necessary code to run the 2024 SISAP Polyadic Query demonstration. Follow these steps to run the program using the IntelliJ IDE.

1. Install the "Ant" plugin in IntellijJ from Settings > Plugins.
2. Once installed, change the path of gwt-2.11.0/WebImageTests/config.properties to point to the correct .obj files, which may either be generated or downloaded.
3. Under the "Ant" tool window, click the "devmode" option. This will launch the GWT server.
4. Click "Launch Default Browser" from the server. This will load the UI, which can be used as described in the paper.

## Downloading an HNSW Index
To download the HNSW index:
1. Navigate to https://similarity.cs.st-andrews.ac.uk/PolyDemo/mf_dino_sm10_hnsw_1m_l2.obj
2. After the index file has downloaded, ensure the index-path-dino2-l2 property of properties.config is pointing to the index.

## Building an HNSW Index
To build the index from scratch:

1. Navigate to uk.ac.standrews.cs.CreateMsedDinoHnsw
2. Ensure that the path-root directory described in config.properties contains the Dinov2 data. The folder should contain 1,000 txt files, named 0.txt, 1.txt, and so on, each of 1,000 lines.  Each line should be the 384 dimensional Dinov2s vector of the corresponding image in MirFlickr.
3. Run the main method of CreateMsedDinoHnsw. This will construct the index.
