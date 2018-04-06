package com.pentateuch.watersupply.utils;

interface AsyncTaskListener<Params, Progress, Result> {
    Result onBeginExecution(Params[] params);

    void onPreExecution();

    void onPostExecution(Result result);
}