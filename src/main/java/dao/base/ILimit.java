package dao.base;

public interface ILimit {
	LimitCondition limit(int page, int size);
}
