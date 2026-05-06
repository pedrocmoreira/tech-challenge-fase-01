-- ============================================================
-- SEED — GarageSystem
-- Dados de exemplo para desenvolvimento e demonstração
-- Execute após as migrations do Flyway.
-- Idempotente: limpa os dados existentes antes de inserir.
-- ============================================================

-- Limpeza (ordem inversa de FK)
TRUNCATE TABLE part_items    CASCADE;
TRUNCATE TABLE service_items CASCADE;
TRUNCATE TABLE service_orders CASCADE;
TRUNCATE TABLE vehicles       CASCADE;
TRUNCATE TABLE parts          CASCADE;
TRUNCATE TABLE services       CASCADE;
TRUNCATE TABLE customers      CASCADE;

-- ============================================================
-- CLIENTES
-- ============================================================
INSERT INTO customers (name, document, document_type, phone, email) VALUES
('João Silva',               '123.456.789-00',     'CPF',  '(11) 91234-5678', 'joao.silva@email.com'),
('Maria Oliveira',           '987.654.321-00',     'CPF',  '(11) 98765-4321', 'maria.oliveira@email.com'),
('Roberto Costa',            '456.789.123-00',     'CPF',  '(21) 99988-7766', 'roberto.costa@email.com'),
('Fernanda Lima',            '321.654.987-00',     'CPF',  '(31) 98877-6655', 'fernanda.lima@email.com'),
('Transportes Rápido Ltda',  '12.345.678/0001-90', 'CNPJ', '(11) 3344-5566',  'frota@transportesrapido.com'),
('Auto Locadora Boa Viagem', '98.765.432/0001-10', 'CNPJ', '(11) 4455-6677',  'frota@boaviagem.com');

-- ============================================================
-- VEÍCULOS
-- ============================================================
INSERT INTO vehicles (plate, make, model, year, color, customer_id) VALUES
('ABC-1234', 'Toyota',     'Corolla XEi',    2021, 'Prata',    (SELECT id FROM customers WHERE document = '123.456.789-00')),
('DEF-5678', 'Honda',      'Civic EXL',      2020, 'Preto',    (SELECT id FROM customers WHERE document = '987.654.321-00')),
('GHI-9012', 'Volkswagen', 'Golf GTI',       2022, 'Branco',   (SELECT id FROM customers WHERE document = '456.789.123-00')),
('JKL-3456', 'Chevrolet',  'Onix Plus',      2019, 'Vermelho', (SELECT id FROM customers WHERE document = '321.654.987-00')),
('MNO-7890', 'Ford',       'Transit Cargo',  2020, 'Branco',   (SELECT id FROM customers WHERE document = '12.345.678/0001-90')),
('PQR-1122', 'Ford',       'Transit Cargo',  2021, 'Branco',   (SELECT id FROM customers WHERE document = '12.345.678/0001-90')),
('STU-3344', 'Fiat',       'Strada Freedom', 2022, 'Cinza',    (SELECT id FROM customers WHERE document = '98.765.432/0001-10')),
('VWX-5566', 'Renault',    'Kwid Zen',       2023, 'Laranja',  (SELECT id FROM customers WHERE document = '98.765.432/0001-10'));

-- ============================================================
-- SERVIÇOS
-- ============================================================
INSERT INTO services (name, description, base_price, estimated_time_minutes, active) VALUES
('Troca de Óleo e Filtro',              'Substituição do óleo do motor e filtro de óleo. Inclui verificação do nível dos demais fluidos.',    180.00,  60, true),
('Alinhamento e Balanceamento',         'Correção do alinhamento das rodas e balanceamento com chumbo de equilíbrio.',                        150.00,  90, true),
('Revisão dos Freios',                  'Verificação e substituição de pastilhas, discos e fluido de freio quando necessário.',               350.00, 120, true),
('Diagnóstico Eletrônico',              'Leitura dos códigos de falha via scanner OBD2. Relatório detalhado de erros encontrados.',           120.00,  45, true),
('Troca de Correia Dentada',            'Substituição da correia dentada e tensor. Recomendada a cada 60.000 km.',                            650.00, 180, true),
('Revisão do Sistema de Arrefecimento', 'Verificação e troca do fluido de arrefecimento, mangueiras e tampa do radiador.',                   280.00,  90, true),
('Troca de Amortecedores',              'Substituição dos amortecedores dianteiros e/ou traseiros.',                                          800.00, 150, true),
('Limpeza de Bicos Injetores',          'Limpeza ultrassônica dos bicos injetores. Melhora desempenho e reduz consumo.',                     320.00,  90, true),
('Troca de Embreagem',                  'Substituição do kit de embreagem completo: disco, plato e rolamento.',                              1200.00, 240, true),
('Higienização do Ar-Condicionado',     'Limpeza do evaporador e canaletas. Elimina odores e fungos.',                                       180.00,  60, true),
('Geometria e Caster',                  'Regulagem completa de geometria, caster e cambagem para suspensão independente.',                    220.00,  90, true),
('Troca de Velas de Ignição',           'Substituição das velas de ignição. Melhora a partida e reduz o consumo.',                           280.00,  60, true);

-- ============================================================
-- PEÇAS / INSUMOS
-- ============================================================
INSERT INTO parts (name, description, code, unit_price, stock_quantity, min_stock, active) VALUES
-- Óleos e fluidos
('Óleo Motor 5W30 Sintético 1L',       'Óleo sintético para motores flex e gasolina',           'OL-5W30-1L',  28.90, 120, 20, true),
('Óleo Motor 5W40 Full Sintético 1L',  'Óleo full sintético para veículos de alto desempenho',  'OL-5W40-1L',  34.90,  80, 15, true),
('Fluido de Freio DOT4 500ml',         'Fluido de freio para sistemas ABS e convencionais',      'FF-DOT4',     22.50,  60, 10, true),
('Fluido de Arrefecimento 1L',         'Fluido verde para sistemas de arrefecimento',             'FA-VERDE-1L', 18.90,  90, 15, true),
('Fluido de Direção Hidráulica 1L',    'Fluido para sistemas de direção hidráulica',              'FDH-1L',      24.90,  40, 10, true),
-- Filtros
('Filtro de Óleo Universal',           'Filtro de óleo compatível com múltiplos modelos',         'FO-UNIV',     32.00,  85, 15, true),
('Filtro de Ar Universal',             'Filtro de ar para motores aspirados',                      'FAR-UNIV',    45.00,  70, 10, true),
('Filtro de Combustível Universal',    'Filtro de linha para combustível',                         'FCB-UNIV',    38.00,  55, 10, true),
-- Freios
('Pastilha de Freio Dianteira (jogo)', 'Jogo de pastilhas dianteiras para veículos populares',    'PFD-POP',     89.90,  40,  8, true),
('Disco de Freio Dianteiro (par)',     'Par de discos ventilados para veículos populares',         'DFD-POP',    210.00,  20,  4, true),
('Pastilha de Freio Traseira (jogo)',  'Jogo de pastilhas traseiras para veículos populares',     'PFT-POP',     79.90,  35,  8, true),
-- Correias e tensores
('Correia Dentada Universal',          'Correia dentada compatível com motores 1.0 e 1.4',        'CD-UNIV',    145.00,  25,  5, true),
('Tensor Correia Dentada',             'Tensor automático para correia dentada',                   'TCD-UNIV',    98.00,  20,  5, true),
-- Amortecedores
('Amortecedor Dianteiro',              'Amortecedor dianteiro para veículos populares',            'AMD-POP',    285.00,  16,  4, true),
('Amortecedor Traseiro',               'Amortecedor traseiro para veículos populares',             'AMT-POP',    240.00,  16,  4, true),
-- Ignição
('Vela de Ignição NGK (jogo 4)',       'Jogo de 4 velas NGK para motores 4 cilindros',             'VIG-NGK4',    72.00,  50, 10, true),
-- Embreagem
('Kit Embreagem Completo 1.0/1.4',     'Disco, plato e rolamento para motores 1.0 e 1.4',         'KEC-10-14',  480.00,  10,  2, true),
-- Ar-condicionado
('Produto Higienização A/C 400ml',     'Spray higienizador para sistema de ar-condicionado',      'HAC-400',     45.00,  30,  8, true);

-- ============================================================
-- ORDENS DE SERVIÇO
-- ============================================================

-- OS-0001 — ENTREGUE (histórico completo)
INSERT INTO service_orders (number, status, observations, created_at, diagnosis_start_date, execution_start_date, completion_date, delivery_date, customer_id, vehicle_id) VALUES
('OS-2024-0001', 'ENTREGUE',
 'Veículo entregue sem pendências. Cliente satisfeito.',
 NOW() - INTERVAL '30 days',
 NOW() - INTERVAL '30 days' + INTERVAL '2 hours',
 NOW() - INTERVAL '29 days' + INTERVAL '3 hours',
 NOW() - INTERVAL '28 days',
 NOW() - INTERVAL '27 days',
 (SELECT id FROM customers WHERE document = '123.456.789-00'),
 (SELECT id FROM vehicles  WHERE plate    = 'ABC-1234'));

-- OS-0002 — FINALIZADA (aguardando retirada)
INSERT INTO service_orders (number, status, observations, created_at, diagnosis_start_date, execution_start_date, completion_date, customer_id, vehicle_id) VALUES
('OS-2024-0002', 'FINALIZADA',
 'Troca de correia dentada realizada conforme manual. Recomendada nova revisão em 60.000 km.',
 NOW() - INTERVAL '5 days',
 NOW() - INTERVAL '5 days'  + INTERVAL '1 hour',
 NOW() - INTERVAL '4 days'  + INTERVAL '2 hours',
 NOW() - INTERVAL '1 day',
 (SELECT id FROM customers WHERE document = '987.654.321-00'),
 (SELECT id FROM vehicles  WHERE plate    = 'DEF-5678'));

-- OS-0003 — EM EXECUÇÃO
INSERT INTO service_orders (number, status, observations, created_at, diagnosis_start_date, execution_start_date, customer_id, vehicle_id) VALUES
('OS-2024-0003', 'EM_EXECUCAO',
 'Barulho na suspensão dianteira. Diagnóstico indicou amortecedores desgastados.',
 NOW() - INTERVAL '2 days',
 NOW() - INTERVAL '2 days' + INTERVAL '3 hours',
 NOW() - INTERVAL '1 day'  + INTERVAL '8 hours',
 (SELECT id FROM customers WHERE document = '456.789.123-00'),
 (SELECT id FROM vehicles  WHERE plate    = 'GHI-9012'));

-- OS-0004 — AGUARDANDO APROVAÇÃO
INSERT INTO service_orders (number, status, observations, created_at, diagnosis_start_date, customer_id, vehicle_id) VALUES
('OS-2024-0004', 'AGUARDANDO_APROVACAO',
 'Revisão dos 90.000 km. Freios traseiros desgastados e velas a substituir.',
 NOW() - INTERVAL '1 day',
 NOW() - INTERVAL '1 day' + INTERVAL '2 hours',
 (SELECT id FROM customers WHERE document = '321.654.987-00'),
 (SELECT id FROM vehicles  WHERE plate    = 'JKL-3456'));

-- OS-0005 — EM DIAGNÓSTICO (frota)
INSERT INTO service_orders (number, status, observations, created_at, diagnosis_start_date, customer_id, vehicle_id) VALUES
('OS-2024-0005', 'EM_DIAGNOSTICO',
 'Veículo de frota. Manutenção preventiva dos 135.000 km. Verificar sistema completo.',
 NOW() - INTERVAL '3 hours',
 NOW() - INTERVAL '2 hours',
 (SELECT id FROM customers WHERE document = '12.345.678/0001-90'),
 (SELECT id FROM vehicles  WHERE plate    = 'MNO-7890'));

-- OS-0006 — RECEBIDA (entrada recente)
INSERT INTO service_orders (number, status, observations, created_at, customer_id, vehicle_id) VALUES
('OS-2024-0006', 'RECEBIDA',
 'Cliente relata consumo elevado de combustível e luz do motor acesa.',
 NOW() - INTERVAL '30 minutes',
 (SELECT id FROM customers WHERE document = '98.765.432/0001-10'),
 (SELECT id FROM vehicles  WHERE plate    = 'STU-3344'));

-- ============================================================
-- ITENS DE SERVIÇO NAS OS
-- ============================================================

-- OS-0001: troca de óleo + alinhamento
INSERT INTO service_items (service_order_id, service_id, applied_price) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0001'), (SELECT id FROM services WHERE name = 'Troca de Óleo e Filtro'),      180.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0001'), (SELECT id FROM services WHERE name = 'Alinhamento e Balanceamento'), 150.00);

-- OS-0002: correia dentada + diagnóstico
INSERT INTO service_items (service_order_id, service_id, applied_price) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0002'), (SELECT id FROM services WHERE name = 'Troca de Correia Dentada'), 650.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0002'), (SELECT id FROM services WHERE name = 'Diagnóstico Eletrônico'),   120.00);

-- OS-0003: amortecedores + alinhamento + geometria
INSERT INTO service_items (service_order_id, service_id, applied_price) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0003'), (SELECT id FROM services WHERE name = 'Troca de Amortecedores'),      800.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0003'), (SELECT id FROM services WHERE name = 'Alinhamento e Balanceamento'), 150.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0003'), (SELECT id FROM services WHERE name = 'Geometria e Caster'),          220.00);

-- OS-0004: freios + velas + diagnóstico
INSERT INTO service_items (service_order_id, service_id, applied_price) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0004'), (SELECT id FROM services WHERE name = 'Revisão dos Freios'),        350.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0004'), (SELECT id FROM services WHERE name = 'Troca de Velas de Ignição'), 280.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0004'), (SELECT id FROM services WHERE name = 'Diagnóstico Eletrônico'),    120.00);

-- ============================================================
-- ITENS DE PEÇA NAS OS
-- ============================================================

-- OS-0001: 4L óleo 5W30 + filtro de óleo
INSERT INTO part_items (service_order_id, part_id, quantity, unit_price_applied) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0001'), (SELECT id FROM parts WHERE code = 'OL-5W30-1L'), 4, 28.90),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0001'), (SELECT id FROM parts WHERE code = 'FO-UNIV'),    1, 32.00);

-- OS-0002: correia + tensor + 5L óleo 5W40 + filtro
INSERT INTO part_items (service_order_id, part_id, quantity, unit_price_applied) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0002'), (SELECT id FROM parts WHERE code = 'CD-UNIV'),    1, 145.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0002'), (SELECT id FROM parts WHERE code = 'TCD-UNIV'),   1,  98.00),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0002'), (SELECT id FROM parts WHERE code = 'OL-5W40-1L'), 5,  34.90),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0002'), (SELECT id FROM parts WHERE code = 'FO-UNIV'),    1,  32.00);

-- OS-0003: 2 amortecedores dianteiros
INSERT INTO part_items (service_order_id, part_id, quantity, unit_price_applied) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0003'), (SELECT id FROM parts WHERE code = 'AMD-POP'), 2, 285.00);

-- OS-0004: pastilhas traseiras + fluido de freio + velas
INSERT INTO part_items (service_order_id, part_id, quantity, unit_price_applied) VALUES
((SELECT id FROM service_orders WHERE number = 'OS-2024-0004'), (SELECT id FROM parts WHERE code = 'PFT-POP'),  1, 79.90),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0004'), (SELECT id FROM parts WHERE code = 'FF-DOT4'),  1, 22.50),
((SELECT id FROM service_orders WHERE number = 'OS-2024-0004'), (SELECT id FROM parts WHERE code = 'VIG-NGK4'), 1, 72.00);

-- ============================================================
-- RECALCULA total_value de cada OS com base nos itens inseridos
-- ============================================================
UPDATE service_orders so
SET total_value = (
    SELECT COALESCE(SUM(si.applied_price), 0)
    FROM service_items si
    WHERE si.service_order_id = so.id
)
+ (
    SELECT COALESCE(SUM(pi.quantity * pi.unit_price_applied), 0)
    FROM part_items pi
    WHERE pi.service_order_id = so.id
);

-- ============================================================
-- FIM DA SEED
-- ============================================================
