insert into plant_inventory_entry (id, name, description, price) values ('1', 'Mini excavator', '1.5 Tonne Mini excavator', 150);
insert into plant_inventory_entry (id, name, description, price) values ('2', 'Mini excavator', '3 Tonne Mini excavator', 200);
insert into plant_inventory_entry (id, name, description, price) values ('3', 'Midi excavator', '5 Tonne Midi excavator', 250);
insert into plant_inventory_entry (id, name, description, price) values ('4', 'Midi excavator', '8 Tonne Midi excavator', 300);
insert into plant_inventory_entry (id, name, description, price) values ('5', 'Maxi excavator', '15 Tonne Large excavator', 400);
insert into plant_inventory_entry (id, name, description, price) values ('6', 'Maxi excavator', '20 Tonne Large excavator', 450);
insert into plant_inventory_entry (id, name, description, price) values ('7', 'HS dumper', '1.5 Tonne Hi-Swivel Dumper', 150);
insert into plant_inventory_entry (id, name, description, price) values ('8', 'FT dumper', '2 Tonne Front Tip Dumper', 180);
insert into plant_inventory_entry (id, name, description, price) values ('9', 'FT dumper', '2 Tonne Front Tip Dumper', 200);
insert into plant_inventory_entry (id, name, description, price) values ('10', 'FT dumper', '2 Tonne Front Tip Dumper', 300);
insert into plant_inventory_entry (id, name, description, price) values ('11', 'FT dumper', '3 Tonne Front Tip Dumper', 400);
insert into plant_inventory_entry (id, name, description, price) values ('12', 'Loader', 'Hewden Backhoe Loader', 200);
insert into plant_inventory_entry (id, name, description, price) values ('13', 'D-Truck', '15 Tonne Articulating Dump Truck', 250);
insert into plant_inventory_entry (id, name, description, price) values ('14', 'D-Truck', '30 Tonne Articulating Dump Truck', 300);

insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values ('1', '1', 'A01', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values ('4', '1', 'A01_2', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values ('2', '2', 'A02', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values ('3', '3', 'A03', 'UNSERVICEABLEREPAIRABLE');

insert into customer (id, token, email) values('1', 'token', 'buildit228@gmail.com');
INSERT into invoice (id,paid,due_date,paid_Date,amount,purchase_Order_id) VALUES ('134',FALSE,'2017-05-19','2017-06-19',2000,NULL );

INSERT into purchase_order (id,ISSUE_DATE,END_DATE,START_DATE,STATUS,TOTAL,PLANT_ID, ENTRY_ID, CUSTOMER_ID) values ('123','2017-04-15','2017-05-15','2017-04-15','OPEN',1300,'1','1','1');
INSERT into purchase_order (id,ISSUE_DATE,END_DATE,START_DATE,STATUS,TOTAL,PLANT_ID, ENTRY_ID, CUSTOMER_ID) values ('124','2017-05-15','2017-05-25','2017-05-15','OPEN',1325,'2','2','1');
INSERT into purchase_order (id,ISSUE_DATE,END_DATE,START_DATE,STATUS,TOTAL,PLANT_ID, ENTRY_ID, CUSTOMER_ID) values ('125','2017-04-15','1111-11-20','1111-11-11','OPEN',1300,'2','2','1');
INSERT into purchase_order (id,ISSUE_DATE,END_DATE,START_DATE,STATUS,TOTAL,PLANT_ID, ENTRY_ID, CUSTOMER_ID) values ('126','2017-05-15','1111-11-30','1111-11-24','PENDING',1325,'2','2','1');


