package com.pentateuch.watersupply.utils;

import android.os.AsyncTask;

public class AsyncTaskHelper<Params,Progress,Result> extends AsyncTask<Params,Progress,Result> {
    private final AsyncTaskListener<Params,Progress,Result> listener;

    public AsyncTaskHelper(AsyncTaskListener<Params, Progress, Result> listener) {
        this.listener = listener;
    }

    @Override
    protected Result doInBackground(Params[] params) {
        return listener.onBeginExecution(params);
    }

    @Override
    protected void onPreExecute() {
        listener.onPreExecution();
    }

    @Override
    protected void onPostExecute(Result result) {
        listener.onPostExecution(result);
    }
}
