function [weights] = neural_gas_func(data)
    % Parham Nooralishahi
    % Neural Gas Network
    %% Parameter Initialization
    nNUnits = 10;
    nDims = size(data, 2);
    ei = 0.3; ef = 0.05;
    li = 30; lf = 0.01;
    numSamples = length(data);
    t_max = numSamples;
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    fprintf ('Number of Neurons ---> %d\n', nNUnits);
    fprintf ('Input Dimension   ---> %d\n', nDims);
    fprintf ('Number of Signals ---> %d\n', numSamples);
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    %% Initializing weights accroding to P(signal)
    % Edge Weight Matrix
    indexes = randi([1 size(data,1)], [1 nNUnits]);
    neuronWeight = data (indexes,:);
    clear indexes;
    %% Neural Gas Training
    lower_bound = 1;
    section_len = 1000;
    for t = 0:t_max,
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
        %% Order all elements of A, according to their distance to e,
        % i.e. find the sequence of indices (i_0,i_1,…,i_(N-1) ) such
        % that w_i0 is the reference vector closest to ?, w_i1 is the reference
        unitDistance = ones (nNUnits, 1);
        unitDistance = unitDistance * double(buffer);
        unitDistance = abs (sqrt (sum (abs(unitDistance - double(neuronWeight)) .^ 2, 2)));
        % Sort the distances
        [~, unit_index] = sort(unitDistance);
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
        % scatter(neuronWeight(:,1),neuronWeight(:,2),'DisplayName','neuronWeight(:,2) vs. neuronWeight(:,1)','YDataSource','neuronWeight(:,2)');figure(gcf);
    end
    weights = neuronWeight;
end
