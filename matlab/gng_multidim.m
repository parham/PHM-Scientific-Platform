function [weights, connections, errors] = gng_multidim(data, epoch)
    e_b = 0.05;
    e_n = 0.0006;
    age_max = 88;
    landa = 200;
    a_error_rate = 0.5;
    b_error_rate = 0.0005;
    % initialize two first neuron randomly
    neuronsWeight = double([data(randi([1 length(data)]),:); data(randi([1 length(data)]),:)]);
    % initialize connection matrix
    neuronConnections = ones ([2 2]);
    neuronConnections = neuronConnections .* -1;
    signal = 1;
    % initialize error matrix
    local_error = [0; 0];
    num_signal = size(data,1) * epoch;
    %% Training part
    while signal < num_signal,
        %% Generate an input signal
        signal = signal + 1;
        index = randi ([1 length(data)]);
        buffer = data(index,:);
        %% Display the result
        %drawnow
        %nodes = neuronsWeight;
        %edges = neuronConnections;
        %edges (edges ~= -1) = 1;
        %edges (edges == -1) = 0;
        %gplot (edges, nodes, '-*'); hold on;
        %scatter(nodes(:,1),nodes(:,2));
        %labels = num2str ((1:size(nodes,1))','%d');
        %text (nodes(:,1), nodes(:,2), labels, 'horizontal', 'left', 'vertical', 'bottom');
        %scatter(buffer(1), buffer(2), [], [1 0 0], 'filled');
        %xlim ([min(nodes(:,1))-100, max(nodes(:,1))+100]);
        %ylim ([min(nodes(:,2))-100, max(nodes(:,2))+100]);
        %hold off;
        %% Finding Distance
        unitDistance = double(ones (size(neuronsWeight,1), 1));
        unitDistance = unitDistance * double(buffer);
        temp_dis = unitDistance;
        unitDistance = abs (sqrt (sum (abs (unitDistance - neuronsWeight) .^ 2, 2)));
        %% Sort the distances
        [~, unit_index] = sort (unitDistance);
        s1 = unit_index (1);
        s2 = unit_index (2);
        %% Create connection between two closest neuron units
        neuronConnections (s1, s2) = 0;
        neuronConnections (s2, s1) = 0;
        %% Add squared error of winner unit to a local error
        local_error(s1) = local_error(s1) + unitDistance(s1) .^ 2;
        %% Update neuron weights
        % Calculate dw for all neurons (connected to s1 ro not)
        dw = (temp_dis - neuronsWeight) .* e_n;
        % Find neurons are not connected to s1
        % set dw of not connected neurons to ZERO
        dw (neuronConnections (s1,:) == -1, :) = 0;
        dw (s1,:) = (buffer - neuronsWeight (s1,:)) .* e_b;
        % add dw to neurons' weight vector
        neuronsWeight = double(neuronsWeight) + dw;
        %% Increase neurons' connection ages (which connected to s1)
        weight_indexes = neuronConnections (s1,:) ~= -1;
        neuronConnections (s1, weight_indexes) = neuronConnections (s1, weight_indexes) + 1;
        neuronConnections (weight_indexes, s1) = neuronConnections (weight_indexes, s1) + 1;
        %% Remove neuron's connections which their ages are more than maximum
        neuronConnections (neuronConnections > age_max) = -1;
        %% Remove neurons with no connections
        temp = sum (neuronConnections, 2);
        remove_index = find (temp == (-1 * size(neuronConnections, 1)));
        if isempty(remove_index),
            neuronConnections (remove_index, :) = [];
            neuronConnections (:, remove_index) = [];
            neuronsWeight (remove_index, :) = [];
            local_error (remove_index) = [];
        end
        %% Insert New neurons
        if mod (signal, landa) == 0,
            % Find the neural unit with highest accumulated error
            q = find (local_error == max(local_error),1);
            % Find the neighbor of q which has the highest accumulated error
            temp_error = local_error;
            temp_error (neuronConnections (q,:) == -1) = 0;
            f = find (temp_error == max(temp_error),1);
            new_w = (neuronsWeight (q,:) + neuronsWeight (f,:)) / 2;
            % Put new neuron in to the system
            neuronsWeight = [neuronsWeight; new_w];
            % Prepare connection matrix for new node
            neuronConnections (end + 1, :) = -1;
            neuronConnections (:, end + 1) = -1;
            % Remove edge between q and f
            neuronConnections (q, f) = -1;
            neuronConnections (f, q) = -1;
            % Add edge between new node and f and q
            neuronConnections (end, f) = 0;
            neuronConnections (f, end) = 0;
            neuronConnections (end, q) = 0;
            neuronConnections (q, end) = 0;
            % Prepare error vector for new node
            % Decrease Error value of neuron f and q
            local_error (q) = local_error (q) - (a_error_rate * local_error (q));
            local_error (f) = local_error (f) - (a_error_rate * local_error (f));
            % Calculate Error for new node
            local_error (end + 1) = (local_error (q) + local_error (f)) / 2;
        end
        % Decrease error of all units
        local_error = local_error - (local_error .* b_error_rate);
        %scatter(neuronsWeight(:,1),neuronsWeight(:,2),'DisplayName','neuronWeight(:,2) vs. neuronWeight(:,1)','YDataSource','neuronWeight(:,2)');figure(gcf);
    end
    weights = neuronsWeight;
    connections = neuronConnections;
    errors = local_error;
end

