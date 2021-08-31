DROP TYPE IF EXISTS equipmenttype;;
CREATE TYPE equipmenttype as ENUM ('ЦЕНТРОБЕЖНЫЙ_НАСОС','ПОРШНЕВОЙ_НАСОС','КОМПРЕССОР','ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ','СЕПАРАТОР','УЧАСТОК_ТРУБОПРОВОДА','РВС');;
DROP TYPE IF EXISTS organizationtype;;
CREATE TYPE organizationtype as ENUM ('ВЛАДЕЛЕЦ','ЭКСПЛУАТАНТ','НАДЗОР','СЕРВИС');;
DROP TYPE IF EXISTS sensortype;;
CREATE TYPE sensortype as ENUM ('ДАТЧИК_ДАВЛЕНИЯ','ДАТЧИК_ТЕМПЕРАТУРЫ','ДАТЧИК_ОБОРОТОВ');;