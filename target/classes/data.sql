INSERT INTO equipment (commissioning_date, equipment_type, is_critical, manufacture_date, serial_number)
VALUES ('2006-01-12', 'УЧАСТОК_ТРУБОПРОВОДА', true, '2005-06-22', 'QCNF356007823JJ23'),
       ('2007-12-21', 'ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ', true, '2007-11-03', 'AA97MFJK47568322'),
       ('2020-08-10', 'КОМПРЕССОР', false, '2032-07-02', 'VVWW7008031890IOP');;

INSERT INTO sensor (range_max, range_min, sensor_type, equipment_id)
VALUES (90, 10, 'ДАТЧИК_ДАВЛЕНИЯ', 1),
       (180, -30, 'ДАТЧИК_ТЕМПЕРАТУРЫ', 2),
       (1000, 0, 'ДАТЧИК_ОБОРОТОВ', 2),
       (80, 15, 'ДАТЧИК_ДАВЛЕНИЯ', 3),
       (3000, 0, 'ДАТЧИК_ОБОРОТОВ', 3),
       (50, -50, 'ДАТЧИК_ТЕМПЕРАТУРЫ', 3);;

CREATE OR REPLACE FUNCTION notify_to_subscribe_to_equipment() RETURNS trigger AS $BODY$
BEGIN
    PERFORM pg_notify('new_critical_equipment', NEW.id::text);
    RETURN NEW;
END;
$BODY$
    LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION notify_to_subscribe_to_sensor_if_critical() RETURNS trigger AS $BODY$
DECLARE
    equipment_id bigint;
    critical boolean;
BEGIN
    SELECT id, is_critical INTO equipment_id, critical FROM equipment WHERE id = NEW.equipment_id;
    IF critical IS TRUE THEN
        PERFORM pg_notify('new_critical_sensor', CONCAT(equipment_id, '/', NEW.id));
    END IF;
    RETURN NEW;
END;
$BODY$
    LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION notify_to_unsubscribe_from_equipment() RETURNS trigger AS $BODY$
BEGIN
    PERFORM pg_notify('not_critical_equipment', OLD.id::text);
    RETURN NEW;
END;
$BODY$
    LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION notify_to_unsubscribe_from_sensor() RETURNS trigger AS $BODY$
BEGIN
    PERFORM pg_notify('not_critical_sensor', CONCAT(OLD.equipment_id, '/', OLD.id));
    RETURN NEW;
END;
$BODY$
    LANGUAGE plpgsql;;

CREATE TRIGGER notify_on_update_of_equipment_if_critical
    AFTER UPDATE OF is_critical ON equipment
    FOR EACH ROW
    WHEN (OLD.is_critical = false AND NEW.is_critical = true)
EXECUTE FUNCTION notify_to_subscribe_to_equipment();;

CREATE TRIGGER notify_on_insert_of_sensor
    AFTER INSERT ON sensor
    FOR EACH ROW
EXECUTE FUNCTION notify_to_subscribe_to_sensor_if_critical();;

CREATE TRIGGER notify_on_update_of_equipment_if_not_critical
    AFTER UPDATE OF is_critical ON equipment
    FOR EACH ROW
    WHEN (OLD.is_critical = true AND NEW.is_critical = false)
EXECUTE FUNCTION notify_to_unsubscribe_from_equipment();;

CREATE TRIGGER notify_on_delete_of_equipment
    AFTER DELETE ON equipment
    FOR EACH ROW
    WHEN (OLD.is_critical = true)
EXECUTE FUNCTION notify_to_unsubscribe_from_equipment();;

CREATE TRIGGER notify_on_delete_of_sensor
    AFTER DELETE ON sensor
    FOR EACH ROW
EXECUTE FUNCTION notify_to_unsubscribe_from_sensor();;