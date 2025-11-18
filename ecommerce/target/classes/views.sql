DROP VIEW IF EXISTS vw_produtos_mais_vendidos;
DROP VIEW IF EXISTS vw_produtos_comprados_juntos;


CREATE OR REPLACE VIEW vw_produtos_mais_vendidos AS
SELECT
    p.cd_Produto,
    p.nm_Produto,
    SUM(ip.qt_Item) AS total_vendido
FROM tbitempedido ip
         JOIN tbproduto p ON p.cd_Produto = ip.cd_Produto
         JOIN tbpedido pe ON pe.cd_Pedido = ip.cd_Pedido
WHERE pe.status_pedido IN ('ANDAMENTO', 'FINALIZADO')
GROUP BY p.cd_Produto, p.nm_Produto
ORDER BY total_vendido DESC;


CREATE OR REPLACE VIEW vw_produtos_comprados_juntos AS
SELECT
    a.cd_Produto AS produto_origem,
    b.cd_Produto AS produto_recomendado,
    COUNT(*) AS vezes_comprados_juntos
FROM tbitempedido a
         JOIN tbitempedido b
              ON a.cd_Pedido = b.cd_Pedido
                  AND a.cd_Produto <> b.cd_Produto
         JOIN tbpedido p
              ON p.cd_Pedido = a.cd_Pedido
         JOIN tbproduto p1 ON p1.cd_Produto = a.cd_Produto
         JOIN tbproduto p2 ON p2.cd_Produto = b.cd_Produto
WHERE p.status_pedido IN ('FINALIZADO')
GROUP BY a.cd_Produto, p1.nm_Produto, b.cd_Produto, p2.nm_Produto
HAVING COUNT(*) >= 2
ORDER BY vezes_comprados_juntos DESC;