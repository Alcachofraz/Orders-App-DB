use lassunca_BDg07;

SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- APAGAR TABELAS EXISTENTES:
-- 
-- -----------------------------------------------------------------------------------------------------------------

drop table if exists ESTABELECIMENTO;
drop table if exists TRANSPORTADOR;
drop table if exists FORNECEDOR;
drop table if exists PRODUTO;
drop table if exists FORNECE;
drop table if exists ALIMENTO;
drop table if exists CONFECIONADO_COM;
drop table if exists CONTEM;
drop table if exists MENU;
drop table if exists CLIENTE;
drop table if exists UTILIZADOR;
drop table if exists ENCOMENDA;
drop table if exists REMESSA;
drop table if exists AVALIA;
drop table if exists RECOMENDA;

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- ESTABELECIMENTO:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table ESTABELECIMENTO (
 Cod int not null AUTO_INCREMENT primary key,
 Nome varchar(30),
 ServeComida boolean
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into ESTABELECIMENTO (Nome, ServeComida) values ('McDonalds', true);
insert into ESTABELECIMENTO (Nome, ServeComida) values ('Churrascão', true);
insert into ESTABELECIMENTO (Nome, ServeComida) values ('Starbucks', false);
insert into ESTABELECIMENTO (Nome, ServeComida) values ('Pizza Hut', true);
insert into ESTABELECIMENTO (Nome, ServeComida) values ('Burguer King', true);
insert into ESTABELECIMENTO (Nome, ServeComida) values ('Taberna do Zé', false);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- TRANSPORTADOR:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table TRANSPORTADOR (
 Cod int not null AUTO_INCREMENT primary key,
 Nome varchar(30),
 IsCarMot varchar(3)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into TRANSPORTADOR (Nome, isCarMot) values ('Ubereats', 'Car');
insert into TRANSPORTADOR (Nome, isCarMot) values ('Glovo', 'Car');
insert into TRANSPORTADOR (Nome, isCarMot) values ('NoMenu', 'Mot');

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- FORNECEDOR:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table FORNECEDOR (
 Cod int not null AUTO_INCREMENT primary key,
 Nome varchar(30)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into FORNECEDOR (Nome) values ('Unilever');
insert into FORNECEDOR (Nome) values ('NutriDimension');
insert into FORNECEDOR (Nome) values ('Anamak');

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- PRODUTO:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table PRODUTO (
 Tipo varchar(30),
 Cuu int not null primary key -- não é auto_increment, porque é um código universal (introduzido pelo admin)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into PRODUTO values ('Alho', 0);
insert into PRODUTO values ('Azeite', 1);
insert into PRODUTO values ('Couve', 2);
insert into PRODUTO values ('Batata', 3);
insert into PRODUTO values ('Açúcar', 4);
insert into PRODUTO values ('Hortelã', 5);
insert into PRODUTO values ('Ananás', 6);
insert into PRODUTO values ('Alface', 7);
insert into PRODUTO values ('Cogumelo', 8);
insert into PRODUTO values ('Borrego', 9);
insert into PRODUTO values ('Vaca', 10);
insert into PRODUTO values ('Porco', 11);
insert into PRODUTO values ('Pato', 12);
insert into PRODUTO values ('Salmão', 13);
insert into PRODUTO values ('Truta', 14);
insert into PRODUTO values ('Robalo', 15);
insert into PRODUTO values ('Maçã', 16);
insert into PRODUTO values ('Manga', 17);
insert into PRODUTO values ('Kiwi', 18);
insert into PRODUTO values ('Café', 19);
insert into PRODUTO values ('Leite', 20);
insert into PRODUTO values ('Ovo', 21);
insert into PRODUTO values ('Chocolate', 22);
insert into PRODUTO values ('Arroz', 23);
insert into PRODUTO values ('Limão', 24);
insert into PRODUTO values ('Farinha', 25);
insert into PRODUTO values ('Queijo', 26);
insert into PRODUTO values ('Tomate', 27);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- FORNECE:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table FORNECE (
 FornCod int,
 ProdCuu int,
 EstabCod int,
 Preco float,
 primary Key (ProdCuu, EstabCod), -- O mesmo produto só é fornecido ao estabelecimento por um fornecedor
 constraint ConsFornCod foreign key (FornCod) references FORNECEDOR(Cod),
 constraint ConsProdCuu1 foreign key (ProdCuu) references PRODUTO(Cuu),
 constraint ConsEstabCod1 foreign key (EstabCod) references ESTABELECIMENTO(Cod)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into FORNECE values
(1, 0, 1, 0.22),
(1, 3, 1, 0.28),
(1, 7, 1, 1.26),
(1, 8, 1, 0.39),
(2, 9, 1, 4.60),
(2, 10, 1, 3.40), 
(2, 11, 1, 2.20),
(2, 12, 1, 4.23),
(2, 13, 1, 3.56),
(2, 14, 1, 6.22),
(2, 15, 1, 4.39),
(3, 21, 1, 0.25),
(3, 26, 1, 2.23),
(1, 27, 1, 0.34),

(1, 0, 2, 0.22),
(1, 2, 2, 1.00),
(1, 3, 2, 0.28),
(3, 4, 2, 0.24),
(3, 5, 2, 0.24),
(2, 9, 2, 4.60),
(2, 10, 2, 3.40),
(2, 11, 2, 2.20),
(2, 12, 2, 4.23),
(2, 13, 2, 3.56),
(2, 14, 2, 6.22),
(2, 15, 2, 4.39),
(1, 17, 2, 1.29),
(1, 18, 2, 0.78),
(3, 19, 2, 0.29),
(3, 20, 2, 0.39),
(2, 21, 2, 0.25),
(3, 22, 2, 0.49),
(3, 23, 2, 2.59),
(3, 25, 2, 0.65),
(2, 26, 2, 2.23),

(3, 4, 3, 0.24),
(3, 5, 3, 1.29),
(1, 16, 3, 0.39),
(1, 17, 3, 1.29),
(3, 19, 3, 0.29),
(3, 20, 3, 0.39),
(3, 22, 3, 0.49),
(1, 24, 3, 0.29),

(3, 1, 4, 2.20),
(3, 6, 4, 4.59),
(3, 8, 4, 0.42),
(1, 16, 4, 0.39),
(3, 25, 4, 0.65),
(2, 26, 4, 2.23),
(1, 27, 4, 0.34),

(1, 0, 5, 0.22),
(1, 3, 5, 0.28),
(1, 7, 5, 1.26),
(1, 8, 5, 0.39),
(2, 9, 5, 4.60),
(2, 10, 5, 3.40),
(2, 11, 5, 2.20),
(2, 12, 5, 4.23),
(2, 13, 5, 3.56),
(2, 14, 5, 6.22),
(2, 15, 5, 4.39),
(2, 21, 5, 0.25),
(2, 26, 5, 2.23),
(1, 27, 5, 0.34),

(3, 4, 6, 0.24),
(3, 5, 6, 1.29),
(1, 16, 6, 0.39),
(1, 17, 6, 1.29),
(3, 19, 6, 0.29),
(3, 20, 6, 0.39),
(3, 22, 6, 0.49),
(1, 24, 6, 0.29);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- ALIMENTO:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table ALIMENTO (
 Id int not null AUTO_INCREMENT primary key,
 Nome varchar(30), -- Pode ser comida/bebida
 IsComida boolean
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into ALIMENTO (Nome, IsComida) values ('Lasanha', true);
insert into ALIMENTO (Nome, IsComida) values ('Cozido', true);
insert into ALIMENTO (Nome, IsComida) values ('Maçã', true);
insert into ALIMENTO (Nome, IsComida) values ('Café', false);


insert into ALIMENTO (Nome, IsComida) values ('Gelado de Kiwi', true);
insert into ALIMENTO (Nome, IsComida) values ('Gelado de Manga', true);
insert into ALIMENTO (Nome, IsComida) values ('Gelado de Chocolate', true);
insert into ALIMENTO (Nome, IsComida) values ('Limonada', false);
insert into ALIMENTO (Nome, IsComida) values ('Chocolate Quente', false);
insert into ALIMENTO (Nome, IsComida) values ('Manga', true);
insert into ALIMENTO (Nome, IsComida) values ('Pizza com Ananás', true);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- CONFECIONADO_COM:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table CONFECIONADO_COM (
 AlimId int, 
 ProdCuu int,
 Quant int, -- Quantidade do mesmo produto 'ProdCuu' utilizado na confeção do alimento 'AlimId'
 primary Key (AlimId, ProdCuu),
 constraint ConsAlimId1 foreign key (AlimId) references ALIMENTO(Id),
 constraint ConsProdCuu2 foreign key (ProdCuu) references PRODUTO(Cuu)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into CONFECIONADO_COM values
(1, 0, 1), (1, 10, 1), (1, 11, 1),
(1, 21, 4), (1, 25, 1), (1, 26, 1),
(2, 0, 1), (2, 2, 1), (2, 3, 1),
(2, 11, 1), (2, 23, 1), (3, 16, 1),
(4, 4, 1), (4, 19, 1), (5, 4, 1),
(5, 18, 1), (5, 20, 1), (5, 21, 2),
(6, 4, 1), (6, 17, 1), (6, 20, 1),
(6, 21, 2), (7, 4, 1), (7, 22, 1),
(7, 20, 1), (7, 21, 2), (8, 4, 1),
(8, 5, 1), (8, 24, 4), (9, 4, 1),
(9, 22, 1), (9, 20, 1), (10, 17, 1),
(11, 1, 1), (11, 6, 1), (11, 8, 4),
(11, 25, 1), (11, 26, 1), (11, 27, 4);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- CONTEM:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table CONTEM (
 MenuId int,
 AlimId int,
 Quant int,
 primary Key (MenuId, AlimId),
 constraint ConsMenuId1 foreign key (MenuId) references MENU(Id),
 constraint ConsAlimId2 foreign key (AlimId) references ALIMENTO(Id)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into CONTEM values
(1, 1, 1), (1, 5, 3), (2, 4, 1),
(3, 9, 2), (4, 2, 2), (4, 8, 4),
(4, 10, 2), (5, 2, 2), (5, 7, 4),
(6, 11, 1), (6, 3, 2), (7, 8, 1);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- MENU:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table MENU (
 Id int not null AUTO_INCREMENT primary key,
 Nome varchar(50),
 EstabCod int, -- Estabelecimento que propôs o menu
 NumPessoas int,
 Preco float,
 constraint ConsEstabCod2 foreign key (EstabCod) references ESTABELECIMENTO(Cod)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into MENU (Nome, EstabCod, NumPessoas, Preco) values ('Lasanha e Gelado de Kiwi', 2, 3, 16.99);
insert into MENU (Nome, EstabCod, NumPessoas, Preco) values ('Café', 2, 1, 0.60);
insert into MENU (Nome, EstabCod, NumPessoas, Preco) values ('Chocolate Quente', 3, 2, 2.49);



insert into MENU (Nome, EstabCod, NumPessoas, Preco) values ('Cozido, Limonada e Manga', 2, 4, 29.99);
insert into MENU (Nome, EstabCod, NumPessoas, Preco) values ('Cozido e Gelado de Chocolate', 2, 4, 20.99);
insert into MENU (Nome, EstabCod, NumPessoas, Preco) values ('Piza com Ananás e Maçã', 4, 2, 14.99);
insert into MENU (Nome, EstabCod, NumPessoas, Preco) values ('Limonada', 6, 1, 2.99);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- CLIENTE:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table CLIENTE (
 Cc int(8) not null primary key,
 Nome varchar(40)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into CLIENTE values (59437323, 'Pedro Gonçalves');
insert into CLIENTE values (31942346, 'Maria Albertina');

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- UTILIZADOR:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table UTILIZADOR (
 Id int not null AUTO_INCREMENT primary key,
 Nome varchar(20),
 PalavraPasse int,
 ClienteCc int(8),
 constraint ConsClienteCc1 foreign key (ClienteCc) references CLIENTE(Cc)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into UTILIZADOR (Nome, PalavraPasse, ClienteCc) values ('admin', 92668751, 0);
insert into UTILIZADOR (Nome, PalavraPasse, ClienteCc) values ('alcachofra', 48690, 59437323);
insert into UTILIZADOR (Nome, PalavraPasse, ClienteCc) values ('albertina', -1922948451, 31942346);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- ENCOMENDA:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table ENCOMENDA (
 Id int not null AUTO_INCREMENT primary key,
 DataEnc datetime,
 Destino varchar(60),
 MenuId int,
 ClienteCc int(8),
 constraint ConsMenuId2 foreign key (MenuId) references MENU(Id),
 constraint ConsClienteCc2 foreign key (ClienteCc) references CLIENTE(Cc)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into ENCOMENDA (DataEnc, Destino, MenuId, ClienteCc) values ('2020-12-28 12:43:05', 'Lisboa, Sacavém', 5, 59437323);
insert into ENCOMENDA (DataEnc, Destino, MenuId, ClienteCc) values ('2020-12-29 11:49:54', 'Lisboa, Bobadela', 7, 31942346);
insert into ENCOMENDA (DataEnc, Destino, MenuId, ClienteCc) values ('2020-12-30 11:58:34', 'Lisboa, Moscavide', 3, 31942346);
insert into ENCOMENDA (DataEnc, Destino, MenuId, ClienteCc) values ('2021-01-02 11:52:19', 'Lisboa, Pirescoxe', 1, 59437323);
insert into ENCOMENDA (DataEnc, Destino, MenuId, ClienteCc) values ('2021-01-04 12:23:37', 'Lisboa, Covina', 2, 31942346);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- REMETE:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table REMESSA (
 EncId int not null primary key,
 DataRem datetime,
 TransCod int,
 constraint ConsEnc foreign key (EncId) references ENCOMENDA(Id),
 constraint ConsTransCod foreign key (TransCod) references TRANSPORTADOR(Cod)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into REMESSA (EncId, DataRem, TransCod) values (1, '2020-12-28 12:43:09', 1);
insert into REMESSA (EncId, DataRem, TransCod) values (2, '2020-12-29 12:59:58', 2);
insert into REMESSA (EncId, DataRem, TransCod) values (3, '2020-12-30 12:32:21', 3);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- AVALIACAO:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table AVALIA (
 Id int not null AUTO_INCREMENT primary key,
 Justif varchar(100),
 Pontos int(1), -- 1 a 5
 ClienteCc int(8),
 EstabCod int,
 constraint ConsClienteCc3 foreign key (ClienteCc) references CLIENTE(Cc),
 constraint ConsEstabCod3 foreign key (EstabCod) references ESTABELECIMENTO(Cod)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into AVALIA (Justif, Pontos, ClienteCc, EstabCod) values ('Razoável. A carne tem qualidade, mas a comida chegou fria.', 3, 59437323, 2);
insert into AVALIA (Justif, Pontos, ClienteCc, EstabCod) values ('Chocolate quente delicioso e pacotes de açúcar extra!', 5, 31942346, 3);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- RECOMENDA:
-- 
-- -----------------------------------------------------------------------------------------------------------------

create table RECOMENDA (
 QuemRecomendaCc int(10), -- Cliente que recomenda
 ClienteCc int(10), -- Cliente que recebe recomendação
 MenuId int,
 primary Key (QuemRecomendaCc, ClienteCc, MenuId),
 constraint ConsClienteCc4 foreign key (QuemRecomendaCc) references CLIENTE(Cc),
 constraint ConsClienteCc5 foreign key (ClienteCc) references CLIENTE(Cc),
 constraint ConsMenuId3 foreign key (MenuId) references MENU(Id)
)ENGINE=InnoDB DEFAULT
CHARSET=utf8;

insert into RECOMENDA values (59437323, 31942346, 5);
insert into RECOMENDA values (35734523, 59437323, 2);

-- -----------------------------------------------------------------------------------------------------------------
-- 
-- QUERIES DE TESTE:
-- 
-- -----------------------------------------------------------------------------------------------------------------

-- Menus com um valor entre 10 e 20 euros:
select m.Nome as Menu, m.Preco as Preço, e.Nome as Estabelecimento
from MENU m, ESTABELECIMENTO e
where m.EstabCod = e.Cod and m.Preco > (10 - 0.00001) and m.Preco < (20 + 0.00001) -- 0.00001 por causa da coparação de floats
order by m.Preco;

-- Todos os estabelecimentos e respetivo número de encomendas:
select es.Nome as Estabelecimento, count(m.EstabCod) as Encomendas
from ESTABELECIMENTO es left join (ENCOMENDA en, MENU m)
on en.MenuId = m.Id and m.EstabCod = es.Cod
group by es.Cod
order by Encomendas
desc;

-- Nota: O valores de agregação (group by) nunca devem ser da segunda tabela.

-- O total pago a cada transportador de menus:
select t.Nome as Transportador,
case
	when sum(m.Preco*0.05) is null then 0 else sum(m.Preco*0.05)
end
as Total_Recebido
from TRANSPORTADOR t left join (REMESSA r, ENCOMENDA en, MENU m)
on r.TransCod = t.Cod and r.EncId = en.Id and en.MenuId = m.Id
group by r.TransCod
order by Total_Recebido;

-- Lucro de cada Estabelecimento:
select es.Nome,
case
	when sum(s1.Lucro_Por_Encomenda) is null then 0 else sum(s1.Lucro_Por_Encomenda)
end 
as Lucro
from 
	ESTABELECIMENTO es
left join (
	select m.EstabCod as Code, (m.Preco-m.Preco*0.05)-(c.Quant*sum(f.Preco*cc.Quant)) as Lucro_Por_Encomenda
	from CONTEM c, ALIMENTO a, CONFECIONADO_COM cc, REMESSA r, ENCOMENDA en, MENU m, FORNECE f
	where
		r.EncId = en.Id and en.MenuId = m.Id and
		m.Id = c.MenuId and c.AlimId = a.Id and a.Id = cc.AlimId and
		cc.ProdCuu = f.ProdCuu and f.EstabCod = m.EstabCod
	group by en.Id
) as s1
on es.Cod = s1.Code
group by es.Cod
order by Lucro
desc;

-- O menu mais encomendado em 29/12/2020:
select m.Nome as Menu, count(e.MenuId) as Encomendas
from MENU m, ENCOMENDA e
where m.Id = e.MenuId and year(e.dataEnc) = 2020
group by m.Id
order by Encomendas
desc
limit 1;

-- Nota: Pode fazer-se cast de datetime para date: '2020-12-28 11:44:09' -> '2020-12-28'.

-- Estabelecimentos com maior pontuação média
select es.Nome as Estabelecimento,
case
	when avg(a.Pontos) is null then 0 else avg(a.Pontos)
end
as Pontuação_Média
from ESTABELECIMENTO es left join AVALIA a
on es.Cod = a.EstabCod
group by es.Cod
order by Pontuação_Média
desc;

-- Fornecedores que mais contribuem para a oferta de menus:
select fr.Nome as Fornecedor_Mais_Ativo, count(f.FornCod) as Contrib
from FORNECEDOR fr, FORNECE f
where fr.Cod = f.FornCod
group by f.FornCod 
order by Contrib
desc;

-- Os 3 menus mais recomendados à Maria Albertina:
select m.Nome
from MENU m, CLIENTE c, RECOMENDA r
where c.Nome like 'Maria Albertina' and c.Cc = r.ClienteCc and m.Id = r.MenuId 
group by r.MenuId
order by count(r.MenuId)
desc
limit 3;

-- Ver encomendas ainda sem remessa:
select e.DataEnc, e.Destino, m.Nome, c.Nome
from MENU m, CLIENTE c, ENCOMENDA e 
left join REMESSA r
on e.Id = r.EncId
where m.Id = e.MenuId and c.Cc = e.ClienteCc and r.EncId is null;

-- Ver alimentos que sejam possíveis de confecionar por um determinado estabelecimento, dados os produtos em stock:
select s1.Alimento, s1.AlimentoId
from (
	-- Tabela com quantidade de produtos disponível para cada alimento:
	select a.Nome as Alimento, a.Id as AlimentoId, count(p.Cuu) as ProdQuant
	from CONFECIONADO_COM cc, ALIMENTO a, PRODUTO p, FORNECE f, ESTABELECIMENTO e
	where a.Id = cc.AlimId and cc.ProdCuu = p.Cuu and p.Cuu = f.ProdCuu and 
		f.EstabCod = e.Cod and e.Cod = 4 and (e.ServeComida or !a.IsComida)
	group by AlimentoId
) as s1, (
	-- Tabela com quantidade de produtos necessários para cada alimento:
	select a.Id as AlimentoId, count(p.Cuu) as ProdQuant
	from CONFECIONADO_COM cc, ALIMENTO a, PRODUTO p
	where a.Id = cc.AlimId and cc.ProdCuu = p.Cuu
	group by a.Id
) as s2
where s1.AlimentoId = s2.AlimentoId and s1.ProdQuant = s2.ProdQuant
order by s1.Alimento;

-- Número de menus:
select count(m.Id) from MENU m;

-- Ver produtos disponíveis para um Estabelecimento:
select p.Tipo as Produto, p.Cuu as Cuu
from PRODUTO p, FORNECE f
where p.Cuu = f.ProdCuu and f.EstabCod = 4
order by Produto;

-- Ver produtos que o Estabelecimento nunca comprou
select s1.Produto, s1.Cuu
from (
	-- Ver todos os produtos:
	select p.Tipo as Produto, p.Cuu as Cuu
	from PRODUTO p
) as s1 left join (
	-- Ver produtos disponíveis para um Estabelecimento:
	select p.Tipo as Produto, p.Cuu as Cuu
	from PRODUTO p, FORNECE f
	where p.Cuu = f.ProdCuu and f.EstabCod = 4
) as s2
on s1.Cuu = s2.Cuu
where s2.Cuu is null;

-- Tabelas:
select * from PRODUTO;
select * from CONFECIONADO_COM;
select * from ALIMENTO;
select * from CONTEM;
select * from MENU;
select * from ENCOMENDA;
select * from REMESSA;
select * from ESTABELECIMENTO;
select * from FORNECE;
select * from FORNECEDOR;
select * from TRANSPORTADOR;
select * from RECOMENDA;
select * from AVALIA;
select * from CLIENTE;
select * from UTILIZADOR;

