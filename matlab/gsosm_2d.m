function [] = gsosm_2d (data),
%% Growing Self-Organizing Surface Map
% Parham Nooralishahi

%% Paramters Initialization
indexes = randi ([1 length(data)], [3 1]);
NeuronSet = data(indexes, :);
NeuronCon = zeros (3);
% Maximum local error, which is directly related to the length of
% connections between nodes (triangle edges)
e_max = 0.02;
d_max = e_max * sqrt(3);
delta = d_max - e_max;
% Learning Rate
alpha = 0.5;
% Smallest value accepted for the maximum internal angle of a triangle
theta_min = 0.1
% Minimal correlation rate required to insert a new connection between a
% selected pair of nodes (this parameter constrains the insertion of a 
% wrong connection because GSOSM employs it to identify two distinct 
% parallel sheets of a surface).
betha_min = 0.02;

t_max = 50000
for t = 1:t_max,
    % Generate a new signal
    signal = randi ([1 length(data)]);
    buffer = data (signal, :);
    %% Node insertion operator
    % find distance of all nodes from the signal
    N = ones (length(NeuronSet), 1); 
    N = N * buffer;
    N = abs (sqrt (sum (abs(N - NeuronSet) .^ 2, 2)));
    % find three first nodes with minimum distance
    [~, unit_index] = sort (N);
    s0 = unit_index (1);
    s1 = unit_index (2);
    s2 = unit_index (3);
    % form a traingle T and the incenter w_inc (point equidistant to all
    % triangle edges)
    d_s1s2 = abs (sqrt (sum (abs(N(s1) - N(s2)) .^ 2, 2)));
    d_s0s2 = abs (sqrt (sum (abs(N(s0) - N(s2)) .^ 2, 2)));
    d_s0s1 = abs (sqrt (sum (abs(N(s1) - N(s0)) .^ 2, 2)));
    w_inc = (d_s1s2 * N(s0,:)) + (d_s0s2 * N(s1,:)) + (d_s0s1 * N(s2,:));
    w_inc = w_inc / (d_s1s2 + d_s0s2 + d_s0s1);
    % check if the distance is bigger than d_max - (delta/2)
    if N(s0) >= (d_max - (double(delta)/2)),
        % add new node
        NeuronSet (end+1,:) = buffer;
        % add its connections
        NeuronCon (end+1,:) = 0;
        NeuronCon (:, end+1) = 0;
    end
    %% Adapting GSOSM
    % check if the signal is in intersection between these nodes
    Area_SS0S1 = (1/2.) *  abs(det([buffer(1),      buffer(2),      1; ...
                                    NeuronSet(s0,1),NeuronSet(s0,2),1; ...
                                    NeuronSet(s1,1),NeuronSet(s1,2),1]));
    Area_SS1S2 = (1/2.) *  abs(det([buffer(1),      buffer(2),      1; ...
                                    NeuronSet(s1,1),NeuronSet(s1,2),1; ...
                                    NeuronSet(s2,1),NeuronSet(s2,2),1]));
    Area_SS2S0 = (1/2.) *  abs(det([buffer(1),      buffer(2),      1; ...
                                    NeuronSet(s2,1),NeuronSet(s2,2),1; ...
                                    NeuronSet(s0,1),NeuronSet(s0,2),1]));
    Area_S0S1S2 =(1/2.) *  abs(det([NeuronSet(s0,1),NeuronSet(s0,2),1; ...
                                    NeuronSet(s1,1),NeuronSet(s1,2),1; ...
                                    NeuronSet(s2,1),NeuronSet(s2,2),1]));
    sumA = (Area_SS0S1 + Area_SS1S2 + Area_SS2S0);
    T = isequal (sumA, Area_S0S1S2);
    if T == 1, % inside P
        tU = NeuronSet(s1) - NeuronSet(s0);
        tV = NeuronSet(s2) - NeuronSet(s1);
        Pn = [0 0 (tU(1)*tV(2) - tU(2)*tV(1))];
        NeuronSet(s0) = NeuronSet(s0) + (alpha * (buffer - NeuronSet(s0)));
    else % outside P
        % Update winner's weight
        NeuronSet(s0) = NeuronSet(s0) + (alpha * (buffer - NeuronSet(s0)));
    end
    % First step of adaptation : using SOM adaptation formula
end
