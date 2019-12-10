function [neuronsWeight] = growing_cell_structures(data)
    %% Initialize GCS system
    e_b = 0.06;
    e_n = 0.002;
    alpha = 1;
    beta = 0.0005;
    t_max = 40000;
    landa = 200;
    neuronDim = size (data, 2);
    % Initialize neuron unit reference vectors
    init_indexes = randi([1 size(data,1)], [(neuronDim + 1) 1]);
    neuronsWeight = data (init_indexes, :);
    % Initialize neuron units' connection matrix (with k-dimensionality)
    neuronCon = ones (neuronDim + 1) - eye (neuronDim + 1);
    % Initialize neuron unit's error vector
    local_error = zeros (neuronDim + 1, 1);
    %% Training Process
    for t = 1:t_max,
        % Generate a random signal
        index = randi([1 length(data)]);      
        buffer = data (index, :);
        %% Display the result
        drawnow
        nodes = neuronsWeight;
        edges = neuronCon;
        gplot (edges, nodes, '-*'); hold on;
        scatter(nodes(:,1),nodes(:,2));
        labels = num2str ((1:size(neuronsWeight,1))','%d');
        text (nodes(:,1), nodes(:,2), labels, 'horizontal', 'left', 'vertical', 'bottom');
        scatter(buffer(1), buffer(2), [], [1 0 0], 'filled');
        xlim ([min(nodes(:,1))-100, max(nodes(:,1))+100]);
        ylim ([min(nodes(:,2))-100, max(nodes(:,2))+100]);
        hold off;
        %% Finding Distance
        unitDistance = ones (size(neuronsWeight,1), 1);
        unitDistance = unitDistance * buffer;
        temp_dis = unitDistance;
        unitDistance = abs (sqrt (sum (abs (unitDistance - neuronsWeight) .^ 2, 2)));
        %% Sort the distances
        [~, unit_index] = sort (unitDistance);
        s1 = unit_index (1);
        %% Add squared error of winner unit to a local error
        local_error(s1) = local_error(s1) + unitDistance(s1) .^ 2;
        %% Update neuron units' weight
        % Calculate dw for all neurons (connected to s1 ro not)
        dw = (temp_dis - neuronsWeight) .* e_n;
        % Find neurons are not connected to s1 and
        % set dw of not connected neurons to ZERO
        dw (neuronCon (s1,:) == 0, :) = 0;
        dw (s1,:) = (buffer - neuronsWeight (s1,:)) .* e_b;
        % add dw to neurons' weight vector
        neuronsWeight = neuronsWeight + dw;
        if mod (t, landa) == 0,
            % Find the neuron with maximum error
            q = find (local_error == max(local_error),1);
            % Calculate the neuron with maximum distance with "q"
            ddis = ones (size(neuronsWeight,1), 1);
            ddis = abs(sqrt(sum(((ddis * neuronsWeight(q,:)) - neuronsWeight).^ 2,2)));
            ddis (neuronCon(q,:) == 0) = 0;
            f = find (ddis == max(ddis),1);
            %% Create new node
            w = (neuronsWeight(q,:) + neuronsWeight(f,:)) / 2;
            neuronsWeight (end+1,:) = w;
            %% Create connections
            neuronCon (end+1,:) = 0;
            neuronCon (:,end+1) = 0;
            neuronCon (end, [q f]) = 1;
            neuronCon ([q f], end) = 1;
            neuronCon ([q,f], [q,f]) = 0;
            nc_index = neuronCon (q,:) == 1 & neuronCon (f,:) == 1;
            neuronCon (end, nc_index) = 1;
            neuronCon (nc_index, end) = 1;
            neuronCon (end,end) = 0;
            %% Update errors
            % Update error values of new unit's neighbors
            num_r = double(sum (neuronCon(end,:) > 0));
            local_error (neuronCon(end,:) > 0) = local_error (neuronCon(end,:) > 0) - (local_error (neuronCon(end,:) > 0)*(alpha / num_r));
            % Update new unit's error
            local_error (end+1) = (1 / num_r) * sum (local_error(neuronCon(end,:)>0));
        end
        local_error = local_error - (beta * local_error);
    end
end

