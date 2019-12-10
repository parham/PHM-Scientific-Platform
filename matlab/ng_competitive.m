function [ neuronWeight ] = ng_competitive( data )
    % Parham Nooralishahi
    % Neural Gas Network plus Competitive Learning
    %% Parameter Initialization
    nNUnits = 1000;
    nDims = 2;
    ei = 0.3; ef = 0.05;
    li = 30; lf = 0.01;
    Ti = 20; Tf = 90; %200;
    numSamples = 70000;
    t_max = numSamples;
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    fprintf ('Number of Neurons ---> %d\n', nNUnits);
    fprintf ('Input Dimension   ---> %d\n', nDims);
    fprintf ('Number of Signals ---> %d\n', numSamples);
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    %% Data Definition
    %data = 0:0.01:500;
    %data = [data; sqrt(data .^ 2 + 2)]';
    %data = [data; (data .^ 2)]';
    %data = randi([0 500], [numSamples nDims], 'double');
    %% Initializing weights and edge/age matrices
    % Edge Weight Matrix
    indexes = randi([1 size(data,1)], [1 nNUnits]);
    neuronWeight = data (indexes,:);
    clear indexes;
    %neuronWeight = randi([10 300], [nNUnits nDims], 'double');
    % Edge and Time Matrix
    neuronC = ones ([nNUnits nNUnits]) .* -1;
    %% Neural Gas Training
    lower_bound = 1;
    section_len = 1000;
    for t = 0:t_max,
        % Display the result
        drawnow
        nodes = neuronWeight;
        edges = neuronC;
        edges (find(edges ~= -1)) = 1;
        edges (find(edges == -1)) = 0;
        gplot (edges, nodes, '-*'); hold on;
        scatter(nodes(:,1),nodes(:,2));
        xlim ([min(nodes(:,1))-100, max(nodes(:,1))+100]);
        ylim ([min(nodes(:,2))-100, max(nodes(:,2))+100]);
        hold off;
        %% Generate at random an input signal "e" according to "p(e)"
        %index = randi([1 length(data)]);
        index = randi ([lower_bound, (lower_bound + section_len)]);
        if index > length(data),
            index = randi ([1 length(data)]);
        end
        lower_bound = lower_bound + section_len;
        if lower_bound > length(data),
            lower_bound = 1;
        end
        buffer = data(index,:);
        unitDistance = ones([nNUnits nDims]);
        %% Order all elements of A, according to their distance to e,
        % i.e. find the sequence of indices (i_0,i_1,…,i_(N-1) ) such
        % that w_i0 is the reference vector closest to ?, w_i1 is the reference
        for d = 1:nDims,
            unitDistance (:, d) = unitDistance (:, d) .* buffer (d);
        end
        unitDistance = abs (sqrt (sum ((abs(unitDistance - neuronWeight)) .^ 2, 2)));
        % Sort the distances
        [dis unit_index] = sort(unitDistance);
        i0 = unit_index(1);
        i1 = unit_index(2);
        %% Train the neural units according to their distance between the choosen signal and them
        for unit = 1:nNUnits,
            k (unit) = sum(unitDistance < unitDistance(unit));
            e = ei * ((ef / ei)^(t / t_max));
            l = li * ((lf / li)^(t / t_max));
            h = exp(-k(unit)/l);
            neuronWeight (unit, :) = neuronWeight (unit, :) + ((e * h) .* (buffer - neuronWeight (unit, :)));
        end
        %% Update Connections
        % Create new connections
        neuronC (i0, i1) = 0;
        neuronC (i1, i0) = 0;
        weight_indexes = find (neuronC (i0, :) ~= -1);
        % Increase nodes' ages
        neuronC (i0, weight_indexes) = neuronC (i0, weight_indexes) + 1;
        neuronC (weight_indexes, i0) = neuronC (weight_indexes, i0) + 1;
        % Remove elder ages
        T = Ti * ((Tf * Ti)^(t/t_max));
        neuronC (find(neuronC > T)) = -1;
    end
    %scatter(neuronWeight(:,1),neuronWeight(:,2),'DisplayName','neuronWeight(:,2) vs. neuronWeight(:,1)','YDataSource','neuronWeight(:,2)');figure(gcf);

end

