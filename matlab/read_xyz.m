function [data] = read_xyz(file)
fid = fopen (file);
temp = fscanf (fid, '%f %f %f\n');
data = reshape(temp, 3, length(temp) / 3);
data = data';
fclose(fid);
end

