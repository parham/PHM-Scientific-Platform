
function [ NeuronSet ] = gsosm_3d( data )
%% Growing Self-Organizing Surface Map for 3D space
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
% (theta_min <= cos(108) = -0.309
theta_min = -0.1
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
    % check if the distance is bigger than d_max - (delta/2)
    if N(s0) >= (d_max - (double(delta)/2)),
        % add new node
        NeuronSet (end+1,:) = buffer;
        % add its connections
        NeuronCon (end+1,:) = 0;
        NeuronCon (:, end+1) = 0;
    end
    %% Adapting GSOSM
    % check if the point is in the intersection or not
    v0 = NeuronSet(s2) - NeuronSet(s0);
    v1 = NeuronSet(s1) - NeuronSet(s0);
    v2 = buffer - NeuronSet(s0);
    dot00 = dot (v0,v0);
    dot01 = dot (v0,v1);
    dot02 = dot (v0,v2);
    dot11 = dot (v1,v1);
    dot12 = dot (v1,v2);
    invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
    u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    v = (dot00 * dot12 - dot01 * dot02) * invDenom;
    % check if the signal is in intersection between these nodes
    if (NeuronCon(s0,s1) ~= 1 || NeuronCon(s1,s2) ~= 1 || NeuronCon(s0,s2) ~= 1) || ...
       ((u >= 0) && (v >= 0) && (u + v < 1)) == 0,
        dw = alpha * (buffer - NeuronSet(s0));
    else
        % Calculate Normal vector of the s0s1s2 triangle
        U = NeuronSet(s1) - NeuronSet(s0);
        V = NeuronSet(s2) - NeuronSet(s0);
        Pn = [((U(1) * V(3)) - (U(3) * V(2))), ...
              ((U(3) * V(1)) - (U(1) * V(3))), ...
              ((U(1) * V(2)) - (U(2) * V(1)))];
        TRI = [NeuronSet(s0); NeuronSet(s1); NeuronSet(s2)];
        % Calculate distance between the point and triangle
        pdis = pointTriangleDistance(TRI,buffer);
        dw = (alpha * Pn) * pdis;
    end
    % Update weight of winner neuron (s0)
    NeuronSet(s0) = NeuronSet(s0) + dw;
    %% Merging operator
    % calculate the distance between s0 to s1
    d_s0s1 = abs (sqrt (sum (abs(N(s1) - N(s0)) .^ 2, 2)));
    if d_s0s1 < e_max,
        % Calculate the weight of new node
        NeuronSet(end+1,:) = 0.5 * (NeuronSet(s0) + NeuronSet(s1));
        % Remove the unit from weights vector
        NeuronSet([s0,s1],:) = [];
        % Add merged connections between units
        NeuronCon(end+1,:) = 0;
        NeuronCon(:,end+1) = 0;
        temp = NeuronCon(s0,:) | NeuronCon(s1,:);
        NeuronCon(end,:) = temp;
        NeuronCon(:,end) = temp;
        % Remove connections of two omitted unit
        NeuronCon([s0,s1],:) = [];
        NeuronCon(:,[s0,s1]) = [];
    end
    %% Connection Insertion
    % we have to find nearest neighbors again because after merging step
    % the order may be changed.
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
    d_s1s2 = abs (sqrt (sum (abs(NeuronSet(s1,:) - NeuronSet(s2,:)) .^ 2, 2)));
    d_s0s2 = abs (sqrt (sum (abs(NeuronSet(s0) - NeuronSet(s2)) .^ 2, 2)));
    d_s0s1 = abs (sqrt (sum (abs(NeuronSet(s1) - NeuronSet(s0)) .^ 2, 2)));
    % calculate incentric point
    w_inc = (d_s1s2 * NeuronSet(s0,:)) + (d_s0s2 * NeuronSet(s1,:)) + (d_s0s1 * NeuronSet(s2,:));
    w_inc = w_inc / (d_s1s2 + d_s0s2 + d_s0s1);
    % check s0 to s1
    w_temp = (NeuronSet(s0,:) - NeuronSet(s1,:)) / d_s0s1;
    w_cs0 = (w_inc - NeuronSet(s0,:)) / abs (sqrt (sum (abs(w_inc - NeuronSet(s0,:)) .^ 2, 2)));
    w_cs0 = dot (w_cs0,w_temp);
    w_ss0 = (buffer - NeuronSet(s0,:)) / abs (sqrt (sum (abs(buffer - NeuronSet(s0,:)) .^ 2, 2)));
    w_ss0 = dot (w_ss0,w_temp);
    w_cs1 = (w_inc - NeuronSet(s1,:)) / abs (sqrt (sum (abs(w_inc - NeuronSet(s1,:)) .^ 2, 2)));
    w_cs1 = dot (w_cs1,-w_temp);
    w_ss1 = (buffer - NeuronSet(s1,:)) / abs (sqrt (sum (abs(buffer - NeuronSet(s1,:)) .^ 2, 2)));
    w_ss1 = dot (w_ss1,-w_temp);
    if w_cs0 <= w_ss0 && w_cs1 <= w_ss1,
        % check first restiriction (beta)
        betha = dot((buffer - NeuronSet(s0,:)),(NeuronSet(s1,:) - NeuronSet(s0,:))) / (d_s0s1 ^ 2);
        % check second restriction (theta)
        theta = dot(((NeuronSet(s0,:) - NeuronSet(s2,:)) / d_s0s2),((NeuronSet(s1,:) - NeuronSet(s2,:)) / d_s1s2));
        % check third restriction
        
        d_cs0s1_s = abs (cross(NeuronSet(s1,:)-NeuronSet(s0,:),buffer-NeuronSet(s0))) / abs(NeuronSet(s1,:)-NeuronSet(s0,:));
        landa = (1 + abs(betha - 0.5) + )^-1;
        if (betha >= betha_min && betha <= 0.5) ...
           (theta >= theta_min && theta <= 1)
            
        end
    else
    % check s1 to s2
        w_temp = (NeuronSet(s1,:) - NeuronSet(s2,:)) / d_s1s2;
        w_cs1 = (w_inc - NeuronSet(s1,:)) / abs (sqrt (sum (abs(w_inc - NeuronSet(s1)) .^ 2, 2)));
        w_cs1 = dot(w_cs1,w_temp);
        w_ss1 = (buffer - NeuronSet(s1,:)) / abs (sqrt (sum (abs(buffer - NeuronSet(s1)) .^ 2, 2)));
        w_ss1 = dot(w_ss1,w_temp);
        w_cs2 = (w_inc - NeuronSet(s2,:)) / abs (sqrt (sum (abs(w_inc - NeuronSet(s2)) .^ 2, 2)));
        w_cs2 = dot(w_cs2,-w_temp);
        w_ss2 = (buffer - NeuronSet(s2,:)) / abs (sqrt (sum (abs(buffer - NeuronSet(s2)) .^ 2, 2)));
        w_ss2 = dot(w_ss2,-w_temp);
        if w_cs1 <= w_ss1 && w_cs2 <= w_ss2,
            
        else
            % check s0 to s2
            w_temp = (NeuronSet(s0,:) - NeuronSet(s2,:)) / d_s0s2;
            w_cs0 = (w_inc - NeuronSet(s0,:)) / abs (sqrt (sum (abs(w_inc - NeuronSet(s0)) .^ 2, 2)));
            w_cs0 = dot(w_cs0,w_temp);
            w_ss0 = (buffer - NeuronSet(s0,:)) / abs (sqrt (sum (abs(buffer - NeuronSet(s0)) .^ 2, 2)));
            w_ss0 = dot(w_ss0,w_temp);
            w_cs2 = (w_inc - NeuronSet(s2,:)) / abs (sqrt (sum (abs(w_inc - NeuronSet(s2)) .^ 2, 2)));
            w_cs2 = dot(w_cs2,-w_temp);
            w_ss2 = (buffer - NeuronSet(s2,:)) / abs (sqrt (sum (abs(buffer - NeuronSet(s2)) .^ 2, 2)));
            w_ss2 = dot(w_ss2,-w_temp);
            if w_cs0 <= w_ss0 && w_cs2 <= w_ss2,
                
            end
        end
    end
end

end

