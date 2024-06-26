% Injests matlab data files which are in the format HNSW dataloader
% Creates 1000 1000 line text files from 100 10000 line matlab files.

fs = "";
for i = 1:383
    fs = strcat(fs, "%.12f ");
end

fs = strcat(fs, "%.12f\n");

for i = 0:99
    data = load(strcat(strcat("/Volumes/Data/mf_dino2/", num2str(i)), ".mat"));
    data = data.features;

    for j = 0:9

        %disp( 1 + (1000 * j) )
        %disp((1000 * (j + 1)))
        subset = data(1 + (1000 * j) : 1000 * (j + 1), :);
        fid = fopen(strcat("/Volumes/Data/mf_dino2_text/", strcat(num2str(10 * i + j), ".txt")), 'w');
        disp(10 * i + j);
        fprintf(fid, fs, subset');
        fclose(fid);
    end
end