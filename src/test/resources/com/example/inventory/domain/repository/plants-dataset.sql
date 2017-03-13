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
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values ('2', '2', 'A02', 'SERVICEABLE');
insert into plant_inventory_item (id, plant_info_id, serial_number, equipment_condition) values ('3', '3', 'A03', 'UNSERVICEABLEREPAIRABLE');


insert into plant_reservation (id, plant_id, start_date, end_date) values ('1', '1', '2017-03-22', '2017-03-24');
insert into plant_reservation (id, plant_id, start_date, end_date) values ('2', '2', '2016-04-01', '2016-12-10'); /* for inventory entry booked in last 6 months */
insert into plant_reservation (id, plant_id, start_date, end_date) values ('3', '3', '2015-02-01', '2015-12-10');
insert into plant_reservation (id, plant_id, start_date, end_date) values ('4', '1', '2014-02-01', '2014-12-10');
insert into plant_reservation (id, plant_id, start_date, end_date) values ('5', '2', '2013-02-02', '2013-12-10');
insert into plant_reservation (id, plant_id, start_date, end_date) values ('6', '3', '2012-02-01', '2012-12-10');

insert into maintenance_task (id, description, price, type_of_work, reservation_id, maintenance_plan_id) values ('1', '15 Tonne Articulating Dump Truck', 400,'CORRECTIVE',1,'1');
insert into maintenance_task (id, description, price, type_of_work, reservation_id, maintenance_plan_id) values ('2', '2 Tonne Front Tip Dumper', 250,'CORRECTIVE',2,'2');
insert into maintenance_task (id, description, price, type_of_work, reservation_id, maintenance_plan_id) values ('3', '3 Tonne Mini excavator', 150,'CORRECTIVE',3,'3');

insert into maintenance_task (id, description, price, type_of_work, reservation_id, maintenance_plan_id) values ('4', 'Nice and shiny', 400,'CORRECTIVE',4,'1');
insert into maintenance_task (id, description, price, type_of_work, reservation_id, maintenance_plan_id) values ('5', 'Hewden Backhoe Loader', 100,'PREVENTIVE',5,'1');
insert into maintenance_task (id, description, price, type_of_work, reservation_id) values ('6', 'Nice and shiny', 200,'PREVENTIVE',5);
insert into maintenance_task (id, description, price, type_of_work, reservation_id) values ('7', 'Nice and shiny', 300,'PREVENTIVE',2);
insert into maintenance_task (id, description, price, type_of_work, reservation_id) values ('8', 'Nice and shiny', 400,'PREVENTIVE',3);

insert into maintenance_plan (id, year_of_action, inventory_item_id) values ('1',2017,'1');
insert into maintenance_plan (id, year_of_action, inventory_item_id) values ('2',2016,'2');
insert into maintenance_plan (id, year_of_action, inventory_item_id) values ('3',2015,'3');

insert into maintenance_plan_tasks(maintenance_plan_id,tasks_id) values ('1','1');
insert into maintenance_plan_tasks(maintenance_plan_id,tasks_id) values ('1','4');
insert into maintenance_plan_tasks(maintenance_plan_id,tasks_id) values ('1','5');
insert into maintenance_plan_tasks(maintenance_plan_id,tasks_id) values ('3','3');
insert into maintenance_plan_tasks(maintenance_plan_id,tasks_id) values ('2','2');




