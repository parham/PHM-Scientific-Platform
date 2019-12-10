function [] = display_cloud(nodes, edges)
    % Display the result
    drawnow
    edges (edges ~= -1) = 1;
    edges (edges == -1) = 0;
    gplot (edges, nodes, '-*'); hold on;
    scatter3(nodes(:,1),nodes(:,2),nodes(:,3));
    hold off;
end

